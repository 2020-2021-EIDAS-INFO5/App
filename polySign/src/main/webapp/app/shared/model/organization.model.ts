import { IAuth } from 'app/shared/model/auth.model';

export interface IOrganization {
  id?: number;
  streetAddress?: string;
  postalCode?: string;
  city?: string;
  country?: string;
  vatNumber?: string;
  name?: string;
  auths?: IAuth[];
}

export class Organization implements IOrganization {
  constructor(
    public id?: number,
    public streetAddress?: string,
    public postalCode?: string,
    public city?: string,
    public country?: string,
    public vatNumber?: string,
    public name?: string,
    public auths?: IAuth[]
  ) {}
}
