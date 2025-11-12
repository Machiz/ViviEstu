import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AlojamientoService } from '../../core/services/alojamiento.service';
import { FavoritosService } from '../../core/services/favoritos.service';
import { SolicitudService } from '../../core/services/solicitud.service';
import { ComentarioService } from '../../core/services/comentario.service';
import { InteraccionService } from '../../core/services/interaccion.service';
import { AuthService } from '../../core/services/auth.service';
import { AlojamientoResponse } from '../../core/models/alojamiento.model';
import { ComentarioResponse } from '../../core/models/comentario.model';

@Component({
  selector: 'app-accommodation-detail',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './accommodation-detail.component.html',
  styleUrl: './accommodation-detail.component.scss'
})
export class AccommodationDetailComponent implements OnInit {
  alojamiento?: AlojamientoResponse;
  comentarios: ComentarioResponse[] = [];
  loading: boolean = true;
  showApplicationForm: boolean = false;
  applicationMessage: string = '';
  currentImageIndex: number = 0;

  constructor(
    private route: ActivatedRoute,
    private alojamientoService: AlojamientoService,
    private favoritosService: FavoritosService,
    private solicitudService: SolicitudService,
    private comentarioService: ComentarioService,
    private interaccionService: InteraccionService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadAccommodation(Number(id));
      this.registerView(Number(id));
    }
  }

  loadAccommodation(id: number): void {
    this.alojamientoService.getById(id).subscribe({
      next: (data) => {
        this.alojamiento = data;
        this.loadComentarios(id);
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading accommodation:', error);
        this.loading = false;
      }
    });
  }

  loadComentarios(alojamientoId: number): void {
    this.comentarioService.listarPorAlojamiento(alojamientoId).subscribe({
      next: (data) => {
        this.comentarios = data;
      },
      error: (error) => console.error('Error loading comments:', error)
    });
  }

  registerView(alojamientoId: number): void {
    const user = this.authService.getCurrentUser();
    if (user) {
      this.interaccionService.create({
        estudianteId: user.id,
        alojamientoId: alojamientoId,
        tipo: 'VISTA'
      }).subscribe({
        next: () => console.log('View registered'),
        error: (error) => console.error('Error registering view:', error)
      });
    }
  }

  addToFavorites(): void {
    const user = this.authService.getCurrentUser();
    if (!user || !this.alojamiento) return;

    this.favoritosService.addFavorito(user.id, this.alojamiento.id).subscribe({
      next: () => {
        alert('Agregado a favoritos exitosamente');
      },
      error: (error) => {
        console.error('Error adding to favorites:', error);
        alert('Error al agregar a favoritos');
      }
    });
  }

  submitApplication(): void {
    const user = this.authService.getCurrentUser();
    if (!user || !this.alojamiento) return;

    const request = {
      estudianteId: user.id,
      alojamientoId: this.alojamiento.id,
      mensaje: this.applicationMessage
    };

    this.solicitudService.registrar(request).subscribe({
      next: () => {
        alert('Solicitud enviada exitosamente');
        this.showApplicationForm = false;
        this.applicationMessage = '';
      },
      error: (error) => {
        console.error('Error submitting application:', error);
        alert('Error al enviar solicitud');
      }
    });
  }

  nextImage(): void {
    if (this.alojamiento && this.alojamiento.imagenes) {
      this.currentImageIndex = (this.currentImageIndex + 1) % this.alojamiento.imagenes.length;
    }
  }

  previousImage(): void {
    if (this.alojamiento && this.alojamiento.imagenes) {
      this.currentImageIndex = this.currentImageIndex === 0 
        ? this.alojamiento.imagenes.length - 1 
        : this.currentImageIndex - 1;
    }
  }
}
