// Universidad Models
export interface UniversidadResponse {
  id: number;
  nombre: string;
  direccion?: string;
  latitud?: number;
  longitud?: number;
}

export interface UniversidadRequest {
  nombre: string;
  direccion?: string;
  latitud?: number;
  longitud?: number;
}
