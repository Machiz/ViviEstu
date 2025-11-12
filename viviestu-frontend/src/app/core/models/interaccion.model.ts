// Interaccion Models
export interface InteraccionResponse {
  id: number;
  estudianteId: number;
  alojamientoId: number;
  tipo: string;
  fecha: string;
}

export interface InteraccionRequest {
  estudianteId: number;
  alojamientoId: number;
  tipo: string;
}

export interface InteraccionReporteResponse {
  alojamientoId: number;
  totalVistas: number;
  totalClicks: number;
  totalFavoritos: number;
  totalSolicitudes: number;
  tasaConversion: number;
}
