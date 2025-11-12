import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AlojamientoResponse, AlojamientoRequest } from '../models/alojamiento.model';
import { PropietarioResponse } from '../models/propietario.model';

@Injectable({
  providedIn: 'root'
})
export class AlojamientoService {
  private apiUrl = `${environment.apiUrl}/alojamientos`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<AlojamientoResponse[]> {
    return this.http.get<AlojamientoResponse[]>(this.apiUrl);
  }

  getById(id: number): Observable<AlojamientoResponse> {
    return this.http.get<AlojamientoResponse>(`${this.apiUrl}/${id}`);
  }

  create(request: AlojamientoRequest): Observable<AlojamientoResponse> {
    const formData = new FormData();
    formData.append('titulo', request.titulo);
    formData.append('descripcion', request.descripcion);
    formData.append('direccion', request.direccion);
    formData.append('precioMensual', request.precioMensual.toString());
    formData.append('distritoId', request.distritoId.toString());
    formData.append('nroPartida', request.nroPartida);
    
    if (request.latitud) formData.append('latitud', request.latitud.toString());
    if (request.longitud) formData.append('longitud', request.longitud.toString());
    
    if (request.imagenes && request.imagenes.length > 0) {
      request.imagenes.forEach((imagen, index) => {
        formData.append('imagenes', imagen, imagen.name);
      });
    }
    
    if (request.transporteIds && request.transporteIds.length > 0) {
      request.transporteIds.forEach(id => {
        formData.append('transporteIds', id.toString());
      });
    }
    
    if (request.universidadIds && request.universidadIds.length > 0) {
      request.universidadIds.forEach(id => {
        formData.append('universidadIds', id.toString());
      });
    }

    return this.http.post<AlojamientoResponse>(this.apiUrl, formData);
  }

  update(id: number, request: AlojamientoRequest): Observable<AlojamientoResponse> {
    return this.http.put<AlojamientoResponse>(`${this.apiUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  marcarComoAlquilado(id: number): Observable<AlojamientoResponse> {
    return this.http.put<AlojamientoResponse>(`${this.apiUrl}/${id}/alquilar`, {});
  }

  marcarComoDisponible(id: number): Observable<AlojamientoResponse> {
    return this.http.put<AlojamientoResponse>(`${this.apiUrl}/${id}/liberar`, {});
  }

  listarPorDistrito(distritoId: number): Observable<AlojamientoResponse[]> {
    return this.http.get<AlojamientoResponse[]>(`${this.apiUrl}/distrito/${distritoId}`);
  }

  listarPorUniversidad(universidadId: number): Observable<AlojamientoResponse[]> {
    return this.http.get<AlojamientoResponse[]>(`${this.apiUrl}/universidad/${universidadId}`);
  }

  obtenerDatosVendedor(id: number): Observable<PropietarioResponse> {
    return this.http.get<PropietarioResponse>(`${this.apiUrl}/${id}/vendedor`);
  }

  agregarImagenes(id: number, imagenes: File[]): Observable<AlojamientoResponse> {
    const formData = new FormData();
    imagenes.forEach(imagen => {
      formData.append('imagenes', imagen, imagen.name);
    });
    return this.http.post<AlojamientoResponse>(`${this.apiUrl}/${id}/imagenes`, formData);
  }

  eliminarImagen(alojamientoId: number, imagenId: number): Observable<string> {
    return this.http.delete<string>(`${this.apiUrl}/${alojamientoId}/imagenes/${imagenId}`, {
      responseType: 'text' as 'json'
    });
  }

  buscarPorArea(minLat: number, maxLat: number, minLng: number, maxLng: number): Observable<AlojamientoResponse[]> {
    const params = new HttpParams()
      .set('minLat', minLat.toString())
      .set('maxLat', maxLat.toString())
      .set('minLng', minLng.toString())
      .set('maxLng', maxLng.toString());
    return this.http.get<AlojamientoResponse[]>(`${this.apiUrl}/buscar-por-area`, { params });
  }
}
