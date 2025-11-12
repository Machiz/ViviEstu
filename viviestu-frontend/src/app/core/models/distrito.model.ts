// Distrito Models
export interface DistritoResponse {
  id: number;
  nombre: string;
  descripcion?: string;
  precioPromedio?: number;
  nivelSeguridad?: string;
}

export interface DistritoRequest {
  nombre: string;
  descripcion?: string;
  precioPromedio?: number;
  nivelSeguridad?: string;
}
