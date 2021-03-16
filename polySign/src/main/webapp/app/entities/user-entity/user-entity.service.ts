import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IUserEntity } from 'app/shared/model/user-entity.model';

type EntityResponseType = HttpResponse<IUserEntity>;
type EntityArrayResponseType = HttpResponse<IUserEntity[]>;

@Injectable({ providedIn: 'root' })
export class UserEntityService {
  public resourceUrl = SERVER_API_URL + 'api/user-entities';

  constructor(protected http: HttpClient) {}

  create(userEntity: IUserEntity): Observable<EntityResponseType> {
    return this.http.post<IUserEntity>(this.resourceUrl, userEntity, { observe: 'response' });
  }

  update(userEntity: IUserEntity): Observable<EntityResponseType> {
    return this.http.put<IUserEntity>(this.resourceUrl, userEntity, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserEntity>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserEntity[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  deleteUserEntityByUsername(id: number, username: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/idToDelete/${id}/username/${username}`, { observe: 'response' });
  }
}
