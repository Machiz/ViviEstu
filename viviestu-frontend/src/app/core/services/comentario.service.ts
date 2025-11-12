import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ComentarioResponse, ComentarioRequest } from '../models/comentario.model';

@Injectable({
  providedIn: 'root'
})
export class ComentarioService {
  private apiUrl = `${environment.apiUrl}/comentarios`;

  constructor(private http: HttpClient) {}

  registrar(request: ComentarioRequest): Observable<ComentarioResponse> {
    return this.http.post<ComentarioResponse>(this.apiUrl, request);
  }

  listarPorAlojamiento(alojamientoId: number): Observable<ComentarioResponse[]> {
    return this.http.get<ComentarioResponse[]>(`${this.apiUrl}/alojamiento/${alojamientoId}`);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
