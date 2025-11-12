// User model
export interface User {
  id: number;
  email: string;
  name: string;
  role: 'ESTUDIANTE' | 'PROPIETARIO' | 'ADMIN';
}
