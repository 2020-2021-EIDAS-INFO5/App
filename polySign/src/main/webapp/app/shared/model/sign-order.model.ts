import { ISignedFile } from 'app/shared/model/signed-file.model';
import { IUserEntity } from 'app/shared/model/user-entity.model';
import { ISignatureProcess } from 'app/shared/model/signature-process.model';

export interface ISignOrder {
  id?: number;
  rank?: number;
  signed?: boolean;
  file?: ISignedFile;
  signer?: IUserEntity;
  signature?: ISignatureProcess;
}

export class SignOrder implements ISignOrder {
  constructor(
    public id?: number,
    public rank?: number,
    public signed?: boolean,
    public file?: ISignedFile,
    public signer?: IUserEntity,
    public signature?: ISignatureProcess
  ) {
    this.signed = this.signed || false;
  }
}
