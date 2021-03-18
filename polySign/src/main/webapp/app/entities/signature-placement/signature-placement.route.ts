import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISignaturePlacement, SignaturePlacement } from 'app/shared/model/signature-placement.model';
import { SignaturePlacementService } from './signature-placement.service';
import { SignaturePlacementComponent } from './signature-placement.component';
import { SignaturePlacementDetailComponent } from './signature-placement-detail.component';
import { SignaturePlacementUpdateComponent } from './signature-placement-update.component';

@Injectable({ providedIn: 'root' })
export class SignaturePlacementResolve implements Resolve<ISignaturePlacement> {
  constructor(private service: SignaturePlacementService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISignaturePlacement> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((signaturePlacement: HttpResponse<SignaturePlacement>) => {
          if (signaturePlacement.body) {
            return of(signaturePlacement.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SignaturePlacement());
  }
}

export const signaturePlacementRoute: Routes = [
  {
    path: '',
    component: SignaturePlacementComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'polySignApp.signaturePlacement.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SignaturePlacementDetailComponent,
    resolve: {
      signaturePlacement: SignaturePlacementResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'polySignApp.signaturePlacement.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SignaturePlacementUpdateComponent,
    resolve: {
      signaturePlacement: SignaturePlacementResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'polySignApp.signaturePlacement.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SignaturePlacementUpdateComponent,
    resolve: {
      signaturePlacement: SignaturePlacementResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'polySignApp.signaturePlacement.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
