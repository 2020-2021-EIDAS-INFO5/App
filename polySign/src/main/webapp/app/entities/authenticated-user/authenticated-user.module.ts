import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PolySignSharedModule } from 'app/shared/shared.module';
import { AuthenticatedUserComponent } from './authenticated-user.component';
import { AuthenticatedUserDetailComponent } from './authenticated-user-detail.component';
import { AuthenticatedUserUpdateComponent } from './authenticated-user-update.component';
import { AuthenticatedUserDeleteDialogComponent } from './authenticated-user-delete-dialog.component';
import { authenticatedUserRoute } from './authenticated-user.route';

@NgModule({
  imports: [PolySignSharedModule, RouterModule.forChild(authenticatedUserRoute)],
  declarations: [
    AuthenticatedUserComponent,
    AuthenticatedUserDetailComponent,
    AuthenticatedUserUpdateComponent,
    AuthenticatedUserDeleteDialogComponent,
  ],
  entryComponents: [AuthenticatedUserDeleteDialogComponent],
})
export class PolySignAuthenticatedUserModule {}
