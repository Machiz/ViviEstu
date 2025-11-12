import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { AlojamientoService } from '../../core/services/alojamiento.service';
import { FavoritosService } from '../../core/services/favoritos.service';
import { SolicitudService } from '../../core/services/solicitud.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  userName: string = '';
  recentAccommodations: any[] = [];
  favoritesCount: number = 0;
  applicationsCount: number = 0;
  loading: boolean = true;

  constructor(
    private authService: AuthService,
    private alojamientoService: AlojamientoService,
    private favoritosService: FavoritosService,
    private solicitudService: SolicitudService
  ) {}

  ngOnInit(): void {
    const user = this.authService.getCurrentUser();
    if (user) {
      this.userName = user.name;
      this.loadDashboardData(user.id);
    }
  }

  loadDashboardData(userId: number): void {
    this.alojamientoService.getAll().subscribe({
      next: (data) => {
        this.recentAccommodations = data.slice(0, 6);
      },
      error: (error) => console.error('Error loading accommodations:', error)
    });

    this.favoritosService.getFavoritosByEstudianteId(userId).subscribe({
      next: (data) => {
        this.favoritesCount = data.length;
      },
      error: (error) => console.error('Error loading favorites:', error)
    });

    this.solicitudService.obtenerPorEstudianteId(userId).subscribe({
      next: (data) => {
        this.applicationsCount = data.length;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading applications:', error);
        this.loading = false;
      }
    });
  }

  logout(): void {
    this.authService.logout();
  }
}
