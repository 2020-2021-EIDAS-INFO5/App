import { IAuthenticatedUser } from 'app/shared/model/authenticated-user.model';
import { IOrganization } from 'app/shared/model/organization.model';
import { Role } from 'app/shared/model/enumerations/role.model';

export interface IAuth {
  id?: number;
  hasRole?: Role;
  authenticatedUser?: IAuthenticatedUser;
  organization?: IOrganization;
}

export class Auth implements IAuth {
  constructor(
    public id?: number,
    public hasRole?: Role,
    public authenticatedUser?: IAuthenticatedUser,
    public organization?: IOrganization
  ) {}
}
