// Auth Models
export interface LoginRequest {
  correo: string;
  contrasenia: string;
}

export interface AuthResponse {
  token: string;
  type: string;
  email: string;
  name: string;
}

export interface RegisterEstudianteRequest {
  correo: string;
  contrasenia: string;
  nombre: string;
  apellido: string;
  telefono?: string;
  universidadId?: number;
}

export interface RegisterPropietarioRequest {
  correo: string;
  contrasenia: string;
  nombre: string;
  apellido: string;
  telefono?: string;
  dni?: string;
}
