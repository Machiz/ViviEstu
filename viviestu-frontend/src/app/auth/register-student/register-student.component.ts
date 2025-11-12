import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { UniversidadService } from '../../core/services/universidad.service';
import { UniversidadResponse } from '../../core/models/universidad.model';

@Component({
  selector: 'app-register-student',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './register-student.component.html',
  styleUrl: './register-student.component.scss'
})
export class RegisterStudentComponent implements OnInit {
  registerForm: FormGroup;
  errorMessage: string = '';
  successMessage: string = '';
  loading: boolean = false;
  universidades: UniversidadResponse[] = [];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private universidadService: UniversidadService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(2)]],
      apellido: ['', [Validators.required, Validators.minLength(2)]],
      correo: ['', [Validators.required, Validators.email]],
      telefono: ['', [Validators.pattern(/^[0-9]{9}$/)]],
      contrasenia: ['', [Validators.required, Validators.minLength(8)]],
      confirmarContrasenia: ['', [Validators.required]],
      universidadId: ['']
    }, { validators: this.passwordMatchValidator });
  }

  ngOnInit(): void {
    this.loadUniversidades();
  }

  loadUniversidades(): void {
    this.universidadService.listar().subscribe({
      next: (data) => {
        this.universidades = data;
      },
      error: (error) => {
        console.error('Error loading universities:', error);
      }
    });
  }

  passwordMatchValidator(form: FormGroup) {
    const password = form.get('contrasenia');
    const confirmPassword = form.get('confirmarContrasenia');
    return password && confirmPassword && password.value === confirmPassword.value
      ? null
      : { passwordMismatch: true };
  }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      Object.keys(this.registerForm.controls).forEach(key => {
        this.registerForm.get(key)?.markAsTouched();
      });
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const request = {
      correo: this.registerForm.value.correo,
      contrasenia: this.registerForm.value.contrasenia,
      nombre: this.registerForm.value.nombre,
      apellido: this.registerForm.value.apellido,
      telefono: this.registerForm.value.telefono,
      universidadId: this.registerForm.value.universidadId ? Number(this.registerForm.value.universidadId) : undefined
    };

    this.authService.registerEstudiante(request).subscribe({
      next: (response) => {
        this.successMessage = 'Registro exitoso. Redirigiendo al inicio de sesión...';
        setTimeout(() => {
          this.router.navigate(['/auth/login']);
        }, 2000);
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = error.error?.message || 'Error al registrarse. Por favor, intente nuevamente.';
        this.loading = false;
      }
    });
  }
}
