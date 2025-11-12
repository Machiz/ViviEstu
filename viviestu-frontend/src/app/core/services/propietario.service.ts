import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { PropietarioResponse, PropietarioRequest } from '../models/propietario.model';

@Injectable({
  providedIn: 'root'
})
export class PropietarioService {
  private apiUrl = `${environment.apiUrl}/propietarios`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<PropietarioResponse[]> {
    return this.http.get<PropietarioResponse[]>(this.apiUrl);
  }

  getById(id: number): Observable<PropietarioResponse> {
    return this.http.get<PropietarioResponse>(`${this.apiUrl}/${id}`);
  }

  create(request: PropietarioRequest): Observable<PropietarioResponse> {
    return this.http.post<PropietarioResponse>(this.apiUrl, request);
  }

  update(id: number, request: PropietarioRequest): Observable<PropietarioResponse> {
    return this.http.put<PropietarioResponse>(`${this.apiUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
