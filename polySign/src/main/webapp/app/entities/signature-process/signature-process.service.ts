import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISignatureProcess } from 'app/shared/model/signature-process.model';

type EntityResponseType = HttpResponse<ISignatureProcess>;
type EntityArrayResponseType = HttpResponse<ISignatureProcess[]>;

@Injectable({ providedIn: 'root' })
export class SignatureProcessService {
  public resourceUrl = SERVER_API_URL + 'api/signature-processes';

  constructor(protected http: HttpClient) {}

  create(signatureProcess: ISignatureProcess): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(signatureProcess);
    return this.http
      .post<ISignatureProcess>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(signatureProcess: ISignatureProcess): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(signatureProcess);
    return this.http
      .put<ISignatureProcess>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISignatureProcess>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISignatureProcess[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(signatureProcess: ISignatureProcess): ISignatureProcess {
    const copy: ISignatureProcess = Object.assign({}, signatureProcess, {
      emissionDate:
        signatureProcess.emissionDate && signatureProcess.emissionDate.isValid() ? signatureProcess.emissionDate.toJSON() : undefined,
      expirationDate:
        signatureProcess.expirationDate && signatureProcess.expirationDate.isValid() ? signatureProcess.expirationDate.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.emissionDate = res.body.emissionDate ? moment(res.body.emissionDate) : undefined;
      res.body.expirationDate = res.body.expirationDate ? moment(res.body.expirationDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((signatureProcess: ISignatureProcess) => {
        signatureProcess.emissionDate = signatureProcess.emissionDate ? moment(signatureProcess.emissionDate) : undefined;
        signatureProcess.expirationDate = signatureProcess.expirationDate ? moment(signatureProcess.expirationDate) : undefined;
      });
    }
    return res;
  }
}
