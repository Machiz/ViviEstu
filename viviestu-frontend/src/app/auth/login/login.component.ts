import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  loginForm: FormGroup;
  errorMessage: string = '';
  loading: boolean = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      correo: ['', [Validators.required, Validators.email]],
      contrasenia: ['', [Validators.required]]
    });
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    this.authService.login(this.loginForm.value).subscribe({
      next: (response) => {
        const user = this.authService.getCurrentUser();
        if (user?.role === 'ESTUDIANTE') {
          this.router.navigate(['/student/dashboard']);
        } else if (user?.role === 'PROPIETARIO') {
          this.router.navigate(['/landlord/dashboard']);
        } else {
          this.router.navigate(['/']);
        }
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = error.error?.message || 'Error al iniciar sesión. Verifique sus credenciales.';
        this.loading = false;
      }
    });
  }
}
