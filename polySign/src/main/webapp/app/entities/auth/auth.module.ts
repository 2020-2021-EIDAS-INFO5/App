import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PolySignSharedModule } from 'app/shared/shared.module';
import { AuthComponent } from './auth.component';
import { AuthDetailComponent } from './auth-detail.component';
import { AuthUpdateComponent } from './auth-update.component';
import { AuthDeleteDialogComponent } from './auth-delete-dialog.component';
import { authRoute } from './auth.route';

@NgModule({
  imports: [PolySignSharedModule, RouterModule.forChild(authRoute)],
  declarations: [AuthComponent, AuthDetailComponent, AuthUpdateComponent, AuthDeleteDialogComponent],
  entryComponents: [AuthDeleteDialogComponent],
})
export class PolySignAuthModule {}
