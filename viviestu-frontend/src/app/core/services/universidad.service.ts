import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { UniversidadResponse, UniversidadRequest } from '../models/universidad.model';

@Injectable({
  providedIn: 'root'
})
export class UniversidadService {
  private apiUrl = `${environment.apiUrl}/universidades`;

  constructor(private http: HttpClient) {}

  listar(): Observable<UniversidadResponse[]> {
    return this.http.get<UniversidadResponse[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<UniversidadResponse> {
    return this.http.get<UniversidadResponse>(`${this.apiUrl}/${id}`);
  }

  crear(request: UniversidadRequest): Observable<UniversidadResponse> {
    return this.http.post<UniversidadResponse>(this.apiUrl, request);
  }

  actualizar(id: number, request: UniversidadRequest): Observable<UniversidadResponse> {
    return this.http.put<UniversidadResponse>(`${this.apiUrl}/${id}`, request);
  }

  buscarPorNombre(nombre: string): Observable<UniversidadResponse[]> {
    const params = new HttpParams().set('nombre', nombre);
    return this.http.get<UniversidadResponse[]>(`${this.apiUrl}/buscar`, { params });
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
