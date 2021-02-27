import { IUser } from 'app/core/user/user.model';
import { ISignOrder } from 'app/shared/model/sign-order.model';

export interface IUserEntity {
  id?: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  phone?: string;
  user?: IUser;
  orders?: ISignOrder[];
}

export class UserEntity implements IUserEntity {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public email?: string,
    public phone?: string,
    public user?: IUser,
    public orders?: ISignOrder[]
  ) {}
}
