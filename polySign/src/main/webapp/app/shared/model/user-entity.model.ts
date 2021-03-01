import { IUser } from 'app/core/user/user.model';
import { ISignatureProcess } from 'app/shared/model/signature-process.model';
import { ISignOrder } from 'app/shared/model/sign-order.model';
import { IAuthorit } from 'app/shared/model/authorit.model';

export interface IUserEntity {
  id?: number;
  firstname?: string;
  lastname?: string;
  email?: string;
  phone?: string;
  user?: IUser;
  signatures?: ISignatureProcess[];
  orders?: ISignOrder[];
  authorities?: IAuthorit[];
}

export class UserEntity implements IUserEntity {
  constructor(
    public id?: number,
    public firstname?: string,
    public lastname?: string,
    public email?: string,
    public phone?: string,
    public user?: IUser,
    public signatures?: ISignatureProcess[],
    public orders?: ISignOrder[],
    public authorities?: IAuthorit[]
  ) {}
}
