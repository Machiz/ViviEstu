import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { DistritoResponse, DistritoRequest } from '../models/distrito.model';

@Injectable({
  providedIn: 'root'
})
export class DistritoService {
  private apiUrl = `${environment.apiUrl}/distritos`;

  constructor(private http: HttpClient) {}

  listAll(): Observable<DistritoResponse[]> {
    return this.http.get<DistritoResponse[]>(this.apiUrl);
  }

  getById(id: number): Observable<DistritoResponse> {
    return this.http.get<DistritoResponse>(`${this.apiUrl}/${id}`);
  }

  create(request: DistritoRequest): Observable<DistritoResponse> {
    return this.http.post<DistritoResponse>(this.apiUrl, request);
  }

  update(id: number, request: DistritoRequest): Observable<DistritoResponse> {
    return this.http.put<DistritoResponse>(`${this.apiUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  searchByNombre(nombre: string): Observable<DistritoResponse[]> {
    const params = new HttpParams().set('nombre', nombre);
    return this.http.get<DistritoResponse[]>(`${this.apiUrl}/search`, { params });
  }

  filterByPrecio(precioMin: number, precioMax: number): Observable<DistritoResponse[]> {
    const params = new HttpParams()
      .set('precioMin', precioMin.toString())
      .set('precioMax', precioMax.toString());
    return this.http.get<DistritoResponse[]>(`${this.apiUrl}/filter/precio`, { params });
  }

  filterByTipo(tipo: string): Observable<DistritoResponse[]> {
    const params = new HttpParams().set('tipo', tipo);
    return this.http.get<DistritoResponse[]>(`${this.apiUrl}/filter/tipo`, { params });
  }
}
