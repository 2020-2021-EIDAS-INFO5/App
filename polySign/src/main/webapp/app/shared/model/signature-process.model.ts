import { Moment } from 'moment';
import { ISignedFile } from 'app/shared/model/signed-file.model';
import { ISignOrder } from 'app/shared/model/sign-order.model';
import { IUserEntity } from 'app/shared/model/user-entity.model';
import { Status } from 'app/shared/model/enumerations/status.model';

export interface ISignatureProcess {
  id?: number;
  title?: string;
  emissionDate?: Moment;
  expirationDate?: Moment;
  status?: Status;
  orderedSigning?: boolean;
  finalFile?: ISignedFile;
  signOrders?: ISignOrder[];
  creator?: IUserEntity;
}

export class SignatureProcess implements ISignatureProcess {
  constructor(
    public id?: number,
    public title?: string,
    public emissionDate?: Moment,
    public expirationDate?: Moment,
    public status?: Status,
    public orderedSigning?: boolean,
    public finalFile?: ISignedFile,
    public signOrders?: ISignOrder[],
    public creator?: IUserEntity
  ) {
    this.orderedSigning = this.orderedSigning || false;
  }
}
