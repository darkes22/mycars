import { ICoche } from 'app/shared/model/coche.model';

export interface IIncidente {
  id?: number;
  fallo?: string;
  descripcion?: string;
  coche?: ICoche;
}

export class Incidente implements IIncidente {
  constructor(public id?: number, public fallo?: string, public descripcion?: string, public coche?: ICoche) {}
}
