import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-register-landlord',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './register-landlord.component.html',
  styleUrl: './register-landlord.component.scss'
})
export class RegisterLandlordComponent {
  registerForm: FormGroup;
  errorMessage: string = '';
  successMessage: string = '';
  loading: boolean = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(2)]],
      apellido: ['', [Validators.required, Validators.minLength(2)]],
      correo: ['', [Validators.required, Validators.email]],
      telefono: ['', [Validators.pattern(/^[0-9]{9}$/)]],
      dni: ['', [Validators.required, Validators.pattern(/^[0-9]{8}$/)]],
      contrasenia: ['', [Validators.required, Validators.minLength(8)]],
      confirmarContrasenia: ['', [Validators.required]]
    }, { validators: this.passwordMatchValidator });
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
      dni: this.registerForm.value.dni
    };

    this.authService.registerPropietario(request).subscribe({
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
