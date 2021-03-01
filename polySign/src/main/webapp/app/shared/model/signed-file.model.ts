import { Moment } from 'moment';

export interface ISignedFile {
  id?: number;
  filename?: string;
  fileBytesContentType?: string;
  fileBytes?: any;
  signingDate?: Moment;
  size?: number;
  sha256?: string;
}

export class SignedFile implements ISignedFile {
  constructor(
    public id?: number,
    public filename?: string,
    public fileBytesContentType?: string,
    public fileBytes?: any,
    public signingDate?: Moment,
    public size?: number,
    public sha256?: string
  ) {}
}
