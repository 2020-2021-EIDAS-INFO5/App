import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IUserEntity, UserEntity } from 'app/shared/model/user-entity.model';
import { UserEntityService } from './user-entity.service';
import { UserEntityComponent } from './user-entity.component';
import { UserEntityDetailComponent } from './user-entity-detail.component';
import { UserEntityUpdateComponent } from './user-entity-update.component';

@Injectable({ providedIn: 'root' })
export class UserEntityResolve implements Resolve<IUserEntity> {
  constructor(private service: UserEntityService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUserEntity> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((userEntity: HttpResponse<UserEntity>) => {
          if (userEntity.body) {
            return of(userEntity.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new UserEntity());
  }
}

export const userEntityRoute: Routes = [
  {
    path: '',
    component: UserEntityComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'polySignApp.userEntity.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserEntityDetailComponent,
    resolve: {
      userEntity: UserEntityResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'polySignApp.userEntity.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserEntityUpdateComponent,
    resolve: {
      userEntity: UserEntityResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'polySignApp.userEntity.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserEntityUpdateComponent,
    resolve: {
      userEntity: UserEntityResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'polySignApp.userEntity.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
