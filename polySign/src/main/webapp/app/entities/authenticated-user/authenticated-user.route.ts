import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAuthenticatedUser, AuthenticatedUser } from 'app/shared/model/authenticated-user.model';
import { AuthenticatedUserService } from './authenticated-user.service';
import { AuthenticatedUserComponent } from './authenticated-user.component';
import { AuthenticatedUserDetailComponent } from './authenticated-user-detail.component';
import { AuthenticatedUserUpdateComponent } from './authenticated-user-update.component';

@Injectable({ providedIn: 'root' })
export class AuthenticatedUserResolve implements Resolve<IAuthenticatedUser> {
  constructor(private service: AuthenticatedUserService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAuthenticatedUser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((authenticatedUser: HttpResponse<AuthenticatedUser>) => {
          if (authenticatedUser.body) {
            return of(authenticatedUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AuthenticatedUser());
  }
}

export const authenticatedUserRoute: Routes = [
  {
    path: '',
    component: AuthenticatedUserComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'polySignApp.authenticatedUser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AuthenticatedUserDetailComponent,
    resolve: {
      authenticatedUser: AuthenticatedUserResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'polySignApp.authenticatedUser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AuthenticatedUserUpdateComponent,
    resolve: {
      authenticatedUser: AuthenticatedUserResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'polySignApp.authenticatedUser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AuthenticatedUserUpdateComponent,
    resolve: {
      authenticatedUser: AuthenticatedUserResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'polySignApp.authenticatedUser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
