// Solicitud Models
export interface SolicitudResponse {
  id: number;
  estudianteId: number;
  alojamientoId: number;
  mensaje: string;
  estado: string;
  fechaSolicitud: string;
  fechaActualizacion?: string;
}

export interface SolicitudRequest {
  estudianteId: number;
  alojamientoId: number;
  mensaje: string;
}
