import { ISignedFile } from 'app/shared/model/signed-file.model';
import { IUserEntity } from 'app/shared/model/user-entity.model';
import { SignatureMethod } from 'app/shared/model/enumerations/signature-method.model';

export interface ISignOrder {
  id?: number;
  rank?: number;
  signatureMethod?: SignatureMethod;
  file?: ISignedFile;
  signer?: IUserEntity;
}

export class SignOrder implements ISignOrder {
  constructor(
    public id?: number,
    public rank?: number,
    public signatureMethod?: SignatureMethod,
    public file?: ISignedFile,
    public signer?: IUserEntity
  ) {}
}
