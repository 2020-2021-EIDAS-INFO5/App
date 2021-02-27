import { IUserEntity } from 'app/shared/model/user-entity.model';
import { ISignatureProcess } from 'app/shared/model/signature-process.model';
import { IAuth } from 'app/shared/model/auth.model';

export interface IAuthenticatedUser {
  id?: number;
  user?: IUserEntity;
  signatures?: ISignatureProcess[];
  authorities?: IAuth[];
}

export class AuthenticatedUser implements IAuthenticatedUser {
  constructor(public id?: number, public user?: IUserEntity, public signatures?: ISignatureProcess[], public authorities?: IAuth[]) {}
}
