import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { FavoritosResponse } from '../models/favoritos.model';

@Injectable({
  providedIn: 'root'
})
export class FavoritosService {
  private apiUrl = `${environment.apiUrl}/favoritos`;

  constructor(private http: HttpClient) {}

  getFavoritosByEstudianteId(estudianteId: number): Observable<FavoritosResponse[]> {
    return this.http.get<FavoritosResponse[]>(`${this.apiUrl}/estudiante/${estudianteId}`);
  }

  addFavorito(estudianteId: number, alojamientoId: number): Observable<FavoritosResponse> {
    return this.http.post<FavoritosResponse>(
      `${this.apiUrl}/estudiante/${estudianteId}/alojamiento/${alojamientoId}`,
      {}
    );
  }

  removeFavorito(estudianteId: number, alojamientoId: number): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/estudiante/${estudianteId}/alojamiento/${alojamientoId}`
    );
  }
}
