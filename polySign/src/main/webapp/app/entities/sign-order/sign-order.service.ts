import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISignOrder } from 'app/shared/model/sign-order.model';

type EntityResponseType = HttpResponse<ISignOrder>;
type EntityArrayResponseType = HttpResponse<ISignOrder[]>;

@Injectable({ providedIn: 'root' })
export class SignOrderService {
  public resourceUrl = SERVER_API_URL + 'api/sign-orders';

  constructor(protected http: HttpClient) {}

  create(signOrder: ISignOrder): Observable<EntityResponseType> {
    return this.http.post<ISignOrder>(this.resourceUrl, signOrder, { observe: 'response' });
  }

  update(signOrder: ISignOrder): Observable<EntityResponseType> {
    return this.http.put<ISignOrder>(this.resourceUrl, signOrder, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISignOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISignOrder[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
