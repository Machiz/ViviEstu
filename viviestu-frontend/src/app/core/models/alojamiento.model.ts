// Alojamiento Models
export interface ImagenResponse {
  id: number;
  url: string;
}

export interface AlojamientoResponse {
  id: number;
  titulo: string;
  descripcion: string;
  direccion: string;
  precioMensual: number;
  alquilado: boolean;
  propietario: string;
  distrito: string;
  nroPartida: string;
  fecha: string;
  imagenes: ImagenResponse[];
  transportes: string[];
  universidades: string[];
  latitud: number;
  longitud: number;
}

export interface AlojamientoRequest {
  titulo: string;
  descripcion: string;
  direccion: string;
  precioMensual: number;
  distritoId: number;
  nroPartida: string;
  imagenes?: File[];
  transporteIds?: number[];
  universidadIds?: number[];
  latitud?: number;
  longitud?: number;
}
