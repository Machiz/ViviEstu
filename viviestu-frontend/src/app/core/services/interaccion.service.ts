import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { InteraccionResponse, InteraccionRequest, InteraccionReporteResponse } from '../models/interaccion.model';

@Injectable({
  providedIn: 'root'
})
export class InteraccionService {
  private apiUrl = `${environment.apiUrl}/interacciones`;

  constructor(private http: HttpClient) {}

  getAllInteracciones(): Observable<InteraccionResponse[]> {
    return this.http.get<InteraccionResponse[]>(this.apiUrl);
  }

  getById(id: number): Observable<InteraccionResponse> {
    return this.http.get<InteraccionResponse>(`${this.apiUrl}/${id}`);
  }

  create(request: InteraccionRequest): Observable<InteraccionResponse> {
    return this.http.post<InteraccionResponse>(this.apiUrl, request);
  }

  contarPorAlojamiento(alojamientoId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/contar/${alojamientoId}`);
  }

  update(id: number, request: InteraccionRequest): Observable<InteraccionResponse> {
    return this.http.put<InteraccionResponse>(`${this.apiUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  generarReportePorAlojamiento(alojamientoId: number): Observable<InteraccionReporteResponse> {
    return this.http.get<InteraccionReporteResponse>(`${this.apiUrl}/reporte/${alojamientoId}`);
  }
}
