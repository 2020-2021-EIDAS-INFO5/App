import { IOrganization } from 'app/shared/model/organization.model';
import { IUserEntity } from 'app/shared/model/user-entity.model';
import { Role } from 'app/shared/model/enumerations/role.model';

export interface IAuthorit {
  id?: number;
  hasRole?: Role;
  organization?: IOrganization;
  user?: IUserEntity;
}

export class Authorit implements IAuthorit {
  constructor(public id?: number, public hasRole?: Role, public organization?: IOrganization, public user?: IUserEntity) {}
}
