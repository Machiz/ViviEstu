import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { SolicitudResponse, SolicitudRequest } from '../models/solicitud.model';

@Injectable({
  providedIn: 'root'
})
export class SolicitudService {
  private apiUrl = `${environment.apiUrl}/solicitudes`;

  constructor(private http: HttpClient) {}

  listar(): Observable<SolicitudResponse[]> {
    return this.http.get<SolicitudResponse[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<SolicitudResponse> {
    return this.http.get<SolicitudResponse>(`${this.apiUrl}/${id}`);
  }

  registrar(request: SolicitudRequest): Observable<SolicitudResponse> {
    return this.http.post<SolicitudResponse>(this.apiUrl, request);
  }

  actualizar(id: number, request: SolicitudRequest): Observable<SolicitudResponse> {
    return this.http.put<SolicitudResponse>(`${this.apiUrl}/${id}`, request);
  }

  aceptarSolicitud(id: number): Observable<SolicitudResponse> {
    return this.http.put<SolicitudResponse>(`${this.apiUrl}/${id}/aceptar`, {});
  }

  rechazarSolicitud(id: number): Observable<SolicitudResponse> {
    return this.http.put<SolicitudResponse>(`${this.apiUrl}/${id}/rechazar`, {});
  }

  obtenerPorEstudianteId(id: number): Observable<SolicitudResponse[]> {
    return this.http.get<SolicitudResponse[]>(`${this.apiUrl}/estudiante/${id}`);
  }

  obtenerPorPropietarioId(id: number): Observable<SolicitudResponse[]> {
    return this.http.get<SolicitudResponse[]>(`${this.apiUrl}/propietario/${id}`);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
