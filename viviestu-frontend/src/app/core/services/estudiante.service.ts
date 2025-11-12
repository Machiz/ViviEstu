import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { EstudianteResponse, EstudianteRequest } from '../models/estudiante.model';

@Injectable({
  providedIn: 'root'
})
export class EstudianteService {
  private apiUrl = `${environment.apiUrl}/estudiantes`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<EstudianteResponse[]> {
    return this.http.get<EstudianteResponse[]>(this.apiUrl);
  }

  getById(id: number): Observable<EstudianteResponse> {
    return this.http.get<EstudianteResponse>(`${this.apiUrl}/${id}`);
  }

  create(request: EstudianteRequest): Observable<EstudianteResponse> {
    return this.http.post<EstudianteResponse>(this.apiUrl, request);
  }

  update(id: number, request: EstudianteRequest): Observable<EstudianteResponse> {
    return this.http.put<EstudianteResponse>(`${this.apiUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
