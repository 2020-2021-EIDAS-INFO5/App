import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISignedFile } from 'app/shared/model/signed-file.model';

type EntityResponseType = HttpResponse<ISignedFile>;
type EntityArrayResponseType = HttpResponse<ISignedFile[]>;

@Injectable({ providedIn: 'root' })
export class SignedFileService {
  public resourceUrl = SERVER_API_URL + 'api/signed-files';
  public resourceUrlSignature = SERVER_API_URL + 'api/signed-files/createSignedFileAndSignatureProcess';
  constructor(protected http: HttpClient) {}

  create(signedFile: ISignedFile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(signedFile);
    return this.http
      .post<ISignedFile>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  createSignedFileAndSignatureProcess(signedFile: ISignedFile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(signedFile);
    return this.http
      .post<ISignedFile>(this.resourceUrlSignature, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(signedFile: ISignedFile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(signedFile);
    return this.http
      .put<ISignedFile>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISignedFile>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISignedFile[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(signedFile: ISignedFile): ISignedFile {
    const copy: ISignedFile = Object.assign({}, signedFile, {
      signingDate: signedFile.signingDate && signedFile.signingDate.isValid() ? signedFile.signingDate.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.signingDate = res.body.signingDate ? moment(res.body.signingDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((signedFile: ISignedFile) => {
        signedFile.signingDate = signedFile.signingDate ? moment(signedFile.signingDate) : undefined;
      });
    }
    return res;
  }
}
