import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { AlojamientoService } from '../../core/services/alojamiento.service';
import { DistritoService } from '../../core/services/distrito.service';
import { UniversidadService } from '../../core/services/universidad.service';
import { DistritoResponse } from '../../core/models/distrito.model';
import { UniversidadResponse } from '../../core/models/universidad.model';

@Component({
  selector: 'app-create-accommodation',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './create-accommodation.component.html',
  styleUrl: './create-accommodation.component.scss'
})
export class CreateAccommodationComponent implements OnInit {
  accommodationForm: FormGroup;
  distritos: DistritoResponse[] = [];
  universidades: UniversidadResponse[] = [];
  selectedFiles: File[] = [];
  errorMessage: string = '';
  successMessage: string = '';
  loading: boolean = false;
  isEditMode: boolean = false;
  accommodationId?: number;

  constructor(
    private fb: FormBuilder,
    private alojamientoService: AlojamientoService,
    private distritoService: DistritoService,
    private universidadService: UniversidadService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.accommodationForm = this.fb.group({
      titulo: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(100)]],
      descripcion: ['', [Validators.required, Validators.minLength(20), Validators.maxLength(500)]],
      direccion: ['', [Validators.required]],
      precioMensual: ['', [Validators.required, Validators.min(0)]],
      distritoId: ['', [Validators.required]],
      nroPartida: ['', [Validators.required]],
      latitud: [''],
      longitud: ['']
    });
  }

  ngOnInit(): void {
    this.loadDistritos();
    this.loadUniversidades();

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.accommodationId = Number(id);
      this.loadAccommodation(this.accommodationId);
    }
  }

  loadDistritos(): void {
    this.distritoService.listAll().subscribe({
      next: (data) => {
        this.distritos = data;
      },
      error: (error) => console.error('Error loading districts:', error)
    });
  }

  loadUniversidades(): void {
    this.universidadService.listar().subscribe({
      next: (data) => {
        this.universidades = data;
      },
      error: (error) => console.error('Error loading universities:', error)
    });
  }

  loadAccommodation(id: number): void {
    this.alojamientoService.getById(id).subscribe({
      next: (data) => {
        this.accommodationForm.patchValue({
          titulo: data.titulo,
          descripcion: data.descripcion,
          direccion: data.direccion,
          precioMensual: data.precioMensual,
          nroPartida: data.nroPartida,
          latitud: data.latitud,
          longitud: data.longitud
        });
      },
      error: (error) => console.error('Error loading accommodation:', error)
    });
  }

  onFileSelected(event: any): void {
    const files = event.target.files;
    if (files) {
      this.selectedFiles = Array.from(files);
    }
  }

  onSubmit(): void {
    if (this.accommodationForm.invalid) {
      Object.keys(this.accommodationForm.controls).forEach(key => {
        this.accommodationForm.get(key)?.markAsTouched();
      });
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const request = {
      ...this.accommodationForm.value,
      imagenes: this.selectedFiles
    };

    if (this.isEditMode && this.accommodationId) {
      this.alojamientoService.update(this.accommodationId, request).subscribe({
        next: () => {
          this.successMessage = 'Alojamiento actualizado exitosamente';
          setTimeout(() => {
            this.router.navigate(['/landlord/accommodations']);
          }, 2000);
          this.loading = false;
        },
        error: (error) => {
          this.errorMessage = 'Error al actualizar alojamiento';
          this.loading = false;
        }
      });
    } else {
      this.alojamientoService.create(request).subscribe({
        next: () => {
          this.successMessage = 'Alojamiento creado exitosamente';
          setTimeout(() => {
            this.router.navigate(['/landlord/accommodations']);
          }, 2000);
          this.loading = false;
        },
        error: (error) => {
          this.errorMessage = 'Error al crear alojamiento';
          this.loading = false;
        }
      });
    }
  }
}
