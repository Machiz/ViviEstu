import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
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
  accommodationsCount: number = 0;
  applicationsCount: number = 0;
  loading: boolean = true;

  constructor(
    private authService: AuthService,
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
    this.solicitudService.obtenerPorPropietarioId(userId).subscribe({
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
