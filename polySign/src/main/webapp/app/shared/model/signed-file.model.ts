import { Moment } from 'moment';
import { ISignatureProcess } from 'app/shared/model/signature-process.model';

export interface ISignedFile {
  id?: number;
  filename?: string;
  fileBytesContentType?: string;
  fileBytes?: any;
  signingDate?: Moment;
  size?: number;
  signature?: ISignatureProcess;
}

export class SignedFile implements ISignedFile {
  constructor(
    public id?: number,
    public filename?: string,
    public fileBytesContentType?: string,
    public fileBytes?: any,
    public signingDate?: Moment,
    public size?: number,
    public signature?: ISignatureProcess
  ) {}
}
