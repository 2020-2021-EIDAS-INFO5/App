import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISignaturePlacement } from 'app/shared/model/signature-placement.model';

type EntityResponseType = HttpResponse<ISignaturePlacement>;
type EntityArrayResponseType = HttpResponse<ISignaturePlacement[]>;

@Injectable({ providedIn: 'root' })
export class SignaturePlacementService {
  public resourceUrl = SERVER_API_URL + 'api/signature-placements';

  constructor(protected http: HttpClient) {}

  create(signaturePlacement: ISignaturePlacement): Observable<EntityResponseType> {
    return this.http.post<ISignaturePlacement>(this.resourceUrl, signaturePlacement, { observe: 'response' });
  }

  update(signaturePlacement: ISignaturePlacement): Observable<EntityResponseType> {
    return this.http.put<ISignaturePlacement>(this.resourceUrl, signaturePlacement, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISignaturePlacement>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISignaturePlacement[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
