import { Moment } from 'moment';
import { ISignedFile } from 'app/shared/model/signed-file.model';
import { IAuthenticatedUser } from 'app/shared/model/authenticated-user.model';
import { Status } from 'app/shared/model/enumerations/status.model';

export interface ISignatureProcess {
  id?: number;
  emissionDate?: Moment;
  expirationDate?: Moment;
  title?: string;
  status?: Status;
  orderedSigning?: boolean;
  files?: ISignedFile[];
  creator?: IAuthenticatedUser;
}

export class SignatureProcess implements ISignatureProcess {
  constructor(
    public id?: number,
    public emissionDate?: Moment,
    public expirationDate?: Moment,
    public title?: string,
    public status?: Status,
    public orderedSigning?: boolean,
    public files?: ISignedFile[],
    public creator?: IAuthenticatedUser
  ) {
    this.orderedSigning = this.orderedSigning || false;
  }
}
