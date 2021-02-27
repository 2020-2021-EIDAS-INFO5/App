import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'user-entity',
        loadChildren: () => import('./user-entity/user-entity.module').then(m => m.PolySignUserEntityModule),
      },
      {
        path: 'organization',
        loadChildren: () => import('./organization/organization.module').then(m => m.PolySignOrganizationModule),
      },
      {
        path: 'authenticated-user',
        loadChildren: () => import('./authenticated-user/authenticated-user.module').then(m => m.PolySignAuthenticatedUserModule),
      },
      {
        path: 'auth',
        loadChildren: () => import('./auth/auth.module').then(m => m.PolySignAuthModule),
      },
      {
        path: 'signature-process',
        loadChildren: () => import('./signature-process/signature-process.module').then(m => m.PolySignSignatureProcessModule),
      },
      {
        path: 'signed-file',
        loadChildren: () => import('./signed-file/signed-file.module').then(m => m.PolySignSignedFileModule),
      },
      {
        path: 'sign-order',
        loadChildren: () => import('./sign-order/sign-order.module').then(m => m.PolySignSignOrderModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class PolySignEntityModule {}
