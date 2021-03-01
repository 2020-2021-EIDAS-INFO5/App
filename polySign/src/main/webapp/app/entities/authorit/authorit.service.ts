import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAuthorit } from 'app/shared/model/authorit.model';

type EntityResponseType = HttpResponse<IAuthorit>;
type EntityArrayResponseType = HttpResponse<IAuthorit[]>;

@Injectable({ providedIn: 'root' })
export class AuthoritService {
  public resourceUrl = SERVER_API_URL + 'api/authorits';

  constructor(protected http: HttpClient) {}

  create(authorit: IAuthorit): Observable<EntityResponseType> {
    return this.http.post<IAuthorit>(this.resourceUrl, authorit, { observe: 'response' });
  }

  update(authorit: IAuthorit): Observable<EntityResponseType> {
    return this.http.put<IAuthorit>(this.resourceUrl, authorit, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAuthorit>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAuthorit[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
