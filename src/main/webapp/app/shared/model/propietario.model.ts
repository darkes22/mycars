export interface IPropietario {
  id?: number;
  nombre?: string;
  apellidos?: string;
  dni?: string;
}

export class Propietario implements IPropietario {
  constructor(public id?: number, public nombre?: string, public apellidos?: string, public dni?: string) {}
}
