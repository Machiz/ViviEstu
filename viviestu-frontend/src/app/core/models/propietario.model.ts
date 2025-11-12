// Propietario Models
export interface PropietarioResponse {
  id: number;
  nombre: string;
  apellido: string;
  correo: string;
  telefono: string;
  dni: string;
}

export interface PropietarioRequest {
  nombre: string;
  apellido: string;
  correo: string;
  telefono?: string;
  dni?: string;
}
