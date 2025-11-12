import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { LoginRequest, AuthResponse, RegisterEstudianteRequest, RegisterPropietarioRequest } from '../models/auth.model';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {
    this.loadUserFromSession();
  }

  private loadUserFromSession(): void {
    const token = this.getToken();
    if (token) {
      const user = this.getUserFromSession();
      if (user) {
        this.currentUserSubject.next(user);
      }
    }
  }

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, request).pipe(
      tap(response => {
        this.setToken(response.token);
        const user = this.decodeToken(response.token);
        this.currentUserSubject.next(user);
        this.saveUserToSession(user);
      })
    );
  }

  registerEstudiante(request: RegisterEstudianteRequest): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/register/estudiante`, request, {
      responseType: 'text' as 'json'
    });
  }

  registerPropietario(request: RegisterPropietarioRequest): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/register/propietario`, request, {
      responseType: 'text' as 'json'
    });
  }

  logout(): void {
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('user');
    this.currentUserSubject.next(null);
  }

  getToken(): string | null {
    return sessionStorage.getItem('token');
  }

  private setToken(token: string): void {
    sessionStorage.setItem('token', token);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  hasRole(role: string): boolean {
    const user = this.getCurrentUser();
    return user?.role === role;
  }

  private decodeToken(token: string): User {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return {
        id: payload.userId || 0,
        email: payload.sub || payload.email || '',
        name: payload.name || '',
        role: payload.role || payload.authorities?.[0]?.replace('ROLE_', '') || 'ESTUDIANTE'
      };
    } catch (error) {
      console.error('Error decoding token:', error);
      return {
        id: 0,
        email: '',
        name: '',
        role: 'ESTUDIANTE'
      };
    }
  }

  private saveUserToSession(user: User): void {
    sessionStorage.setItem('user', JSON.stringify(user));
  }

  private getUserFromSession(): User | null {
    const userStr = sessionStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
  }
}
