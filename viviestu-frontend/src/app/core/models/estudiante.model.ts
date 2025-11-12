// Estudiante Models
export interface EstudianteResponse {
  id: number;
  nombre: string;
  apellido: string;
  correo: string;
  telefono: string;
  universidadId: number;
  universidadNombre?: string;
}

export interface EstudianteRequest {
  nombre: string;
  apellido: string;
  correo: string;
  telefono?: string;
  universidadId?: number;
}
