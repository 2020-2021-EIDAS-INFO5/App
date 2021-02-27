import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAuthenticatedUser } from 'app/shared/model/authenticated-user.model';

type EntityResponseType = HttpResponse<IAuthenticatedUser>;
type EntityArrayResponseType = HttpResponse<IAuthenticatedUser[]>;

@Injectable({ providedIn: 'root' })
export class AuthenticatedUserService {
  public resourceUrl = SERVER_API_URL + 'api/authenticated-users';

  constructor(protected http: HttpClient) {}

  create(authenticatedUser: IAuthenticatedUser): Observable<EntityResponseType> {
    return this.http.post<IAuthenticatedUser>(this.resourceUrl, authenticatedUser, { observe: 'response' });
  }

  update(authenticatedUser: IAuthenticatedUser): Observable<EntityResponseType> {
    return this.http.put<IAuthenticatedUser>(this.resourceUrl, authenticatedUser, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAuthenticatedUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAuthenticatedUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
