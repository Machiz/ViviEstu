import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { FavoritosService } from '../../core/services/favoritos.service';
import { AlojamientoService } from '../../core/services/alojamiento.service';
import { AuthService } from '../../core/services/auth.service';
import { FavoritosResponse } from '../../core/models/favoritos.model';
import { AlojamientoResponse } from '../../core/models/alojamiento.model';

@Component({
  selector: 'app-favorites',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './favorites.component.html',
  styleUrl: './favorites.component.scss'
})
export class FavoritesComponent implements OnInit {
  favoritos: FavoritosResponse[] = [];
  alojamientos: AlojamientoResponse[] = [];
  loading: boolean = true;

  constructor(
    private favoritosService: FavoritosService,
    private alojamientoService: AlojamientoService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const user = this.authService.getCurrentUser();
    if (user) {
      this.loadFavorites(user.id);
    }
  }

  loadFavorites(userId: number): void {
    this.favoritosService.getFavoritosByEstudianteId(userId).subscribe({
      next: (favoritos) => {
        this.favoritos = favoritos;
        this.loadAccommodations();
      },
      error: (error) => {
        console.error('Error loading favorites:', error);
        this.loading = false;
      }
    });
  }

  loadAccommodations(): void {
    const favoritoIds = this.favoritos.map(f => f.alojamientoId);
    
    this.alojamientoService.getAll().subscribe({
      next: (all) => {
        this.alojamientos = all.filter(a => favoritoIds.includes(a.id));
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading accommodations:', error);
        this.loading = false;
      }
    });
  }

  removeFavorite(alojamientoId: number): void {
    const user = this.authService.getCurrentUser();
    if (!user) return;

    this.favoritosService.removeFavorito(user.id, alojamientoId).subscribe({
      next: () => {
        this.alojamientos = this.alojamientos.filter(a => a.id !== alojamientoId);
        this.favoritos = this.favoritos.filter(f => f.alojamientoId !== alojamientoId);
      },
      error: (error) => {
        console.error('Error removing favorite:', error);
        alert('Error al eliminar favorito');
      }
    });
  }

  viewDetails(id: number): void {
    this.router.navigate(['/student/accommodation', id]);
  }
}
