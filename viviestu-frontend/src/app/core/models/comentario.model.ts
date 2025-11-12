// Comentario Models
export interface ComentarioResponse {
  id: number;
  estudianteId: number;
  alojamientoId: number;
  contenido: string;
  calificacion?: number;
  fecha: string;
}

export interface ComentarioRequest {
  estudianteId: number;
  alojamientoId: number;
  contenido: string;
  calificacion?: number;
}
