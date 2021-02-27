import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISignOrder, SignOrder } from 'app/shared/model/sign-order.model';
import { SignOrderService } from './sign-order.service';
import { SignOrderComponent } from './sign-order.component';
import { SignOrderDetailComponent } from './sign-order-detail.component';
import { SignOrderUpdateComponent } from './sign-order-update.component';

@Injectable({ providedIn: 'root' })
export class SignOrderResolve implements Resolve<ISignOrder> {
  constructor(private service: SignOrderService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISignOrder> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((signOrder: HttpResponse<SignOrder>) => {
          if (signOrder.body) {
            return of(signOrder.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SignOrder());
  }
}

export const signOrderRoute: Routes = [
  {
    path: '',
    component: SignOrderComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'polySignApp.signOrder.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SignOrderDetailComponent,
    resolve: {
      signOrder: SignOrderResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'polySignApp.signOrder.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SignOrderUpdateComponent,
    resolve: {
      signOrder: SignOrderResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'polySignApp.signOrder.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SignOrderUpdateComponent,
    resolve: {
      signOrder: SignOrderResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'polySignApp.signOrder.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
