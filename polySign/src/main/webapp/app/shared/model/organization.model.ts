import { IAuthorit } from 'app/shared/model/authorit.model';

export interface IOrganization {
  id?: number;
  name?: string;
  streetAddress?: string;
  postalCode?: string;
  city?: string;
  country?: string;
  vatNumber?: string;
  authorits?: IAuthorit[];
}

export class Organization implements IOrganization {
  constructor(
    public id?: number,
    public name?: string,
    public streetAddress?: string,
    public postalCode?: string,
    public city?: string,
    public country?: string,
    public vatNumber?: string,
    public authorits?: IAuthorit[]
  ) {}
}
