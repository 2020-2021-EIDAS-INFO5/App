import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAuthorit, Authorit } from 'app/shared/model/authorit.model';
import { AuthoritService } from './authorit.service';
import { AuthoritComponent } from './authorit.component';
import { AuthoritDetailComponent } from './authorit-detail.component';
import { AuthoritUpdateComponent } from './authorit-update.component';

@Injectable({ providedIn: 'root' })
export class AuthoritResolve implements Resolve<IAuthorit> {
  constructor(private service: AuthoritService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAuthorit> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((authorit: HttpResponse<Authorit>) => {
          if (authorit.body) {
            return of(authorit.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Authorit());
  }
}

export const authoritRoute: Routes = [
  {
    path: '',
    component: AuthoritComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'polySignApp.authorit.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AuthoritDetailComponent,
    resolve: {
      authorit: AuthoritResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'polySignApp.authorit.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AuthoritUpdateComponent,
    resolve: {
      authorit: AuthoritResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'polySignApp.authorit.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AuthoritUpdateComponent,
    resolve: {
      authorit: AuthoritResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'polySignApp.authorit.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
