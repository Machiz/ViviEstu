import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AlojamientoService } from '../../core/services/alojamiento.service';
import { DistritoService } from '../../core/services/distrito.service';
import { UniversidadService } from '../../core/services/universidad.service';
import { FavoritosService } from '../../core/services/favoritos.service';
import { AuthService } from '../../core/services/auth.service';
import { AlojamientoResponse } from '../../core/models/alojamiento.model';
import { DistritoResponse } from '../../core/models/distrito.model';
import { UniversidadResponse } from '../../core/models/universidad.model';

@Component({
  selector: 'app-search-accommodations',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './search-accommodations.component.html',
  styleUrl: './search-accommodations.component.scss'
})
export class SearchAccommodationsComponent implements OnInit {
  alojamientos: AlojamientoResponse[] = [];
  filteredAlojamientos: AlojamientoResponse[] = [];
  distritos: DistritoResponse[] = [];
  universidades: UniversidadResponse[] = [];
  loading: boolean = true;
  
  // Filters
  selectedDistrito: string = '';
  selectedUniversidad: string = '';
  precioMin: number = 0;
  precioMax: number = 10000;
  soloDisponibles: boolean = true;

  constructor(
    private alojamientoService: AlojamientoService,
    private distritoService: DistritoService,
    private universidadService: UniversidadService,
    private favoritosService: FavoritosService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.loading = true;
    
    this.alojamientoService.getAll().subscribe({
      next: (data) => {
        this.alojamientos = data;
        this.applyFilters();
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading accommodations:', error);
        this.loading = false;
      }
    });

    this.distritoService.listAll().subscribe({
      next: (data) => this.distritos = data,
      error: (error) => console.error('Error loading districts:', error)
    });

    this.universidadService.listar().subscribe({
      next: (data) => this.universidades = data,
      error: (error) => console.error('Error loading universities:', error)
    });
  }

  applyFilters(): void {
    this.filteredAlojamientos = this.alojamientos.filter(aloj => {
      let matches = true;

      if (this.soloDisponibles && aloj.alquilado) {
        matches = false;
      }

      if (this.selectedDistrito && aloj.distrito !== this.selectedDistrito) {
        matches = false;
      }

      if (this.selectedUniversidad && !aloj.universidades?.includes(this.selectedUniversidad)) {
        matches = false;
      }

      if (aloj.precioMensual < this.precioMin || aloj.precioMensual > this.precioMax) {
        matches = false;
      }

      return matches;
    });
  }

  resetFilters(): void {
    this.selectedDistrito = '';
    this.selectedUniversidad = '';
    this.precioMin = 0;
    this.precioMax = 10000;
    this.soloDisponibles = true;
    this.applyFilters();
  }

  addToFavorites(alojamientoId: number): void {
    const user = this.authService.getCurrentUser();
    if (!user) return;

    this.favoritosService.addFavorito(user.id, alojamientoId).subscribe({
      next: () => {
        alert('Agregado a favoritos');
      },
      error: (error) => {
        console.error('Error adding to favorites:', error);
        alert('Error al agregar a favoritos');
      }
    });
  }

  viewDetails(id: number): void {
    this.router.navigate(['/student/accommodation', id]);
  }
}
