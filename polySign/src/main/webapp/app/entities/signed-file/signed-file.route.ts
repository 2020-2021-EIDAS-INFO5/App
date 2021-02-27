import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISignedFile, SignedFile } from 'app/shared/model/signed-file.model';
import { SignedFileService } from './signed-file.service';
import { SignedFileComponent } from './signed-file.component';
import { SignedFileDetailComponent } from './signed-file-detail.component';
import { SignedFileUpdateComponent } from './signed-file-update.component';

@Injectable({ providedIn: 'root' })
export class SignedFileResolve implements Resolve<ISignedFile> {
  constructor(private service: SignedFileService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISignedFile> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((signedFile: HttpResponse<SignedFile>) => {
          if (signedFile.body) {
            return of(signedFile.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SignedFile());
  }
}

export const signedFileRoute: Routes = [
  {
    path: '',
    component: SignedFileComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'polySignApp.signedFile.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SignedFileDetailComponent,
    resolve: {
      signedFile: SignedFileResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'polySignApp.signedFile.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SignedFileUpdateComponent,
    resolve: {
      signedFile: SignedFileResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'polySignApp.signedFile.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SignedFileUpdateComponent,
    resolve: {
      signedFile: SignedFileResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'polySignApp.signedFile.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
