import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PolySignSharedModule } from 'app/shared/shared.module';
import { AuthoritComponent } from './authorit.component';
import { AuthoritDetailComponent } from './authorit-detail.component';
import { AuthoritUpdateComponent } from './authorit-update.component';
import { AuthoritDeleteDialogComponent } from './authorit-delete-dialog.component';
import { authoritRoute } from './authorit.route';

@NgModule({
  imports: [PolySignSharedModule, RouterModule.forChild(authoritRoute)],
  declarations: [AuthoritComponent, AuthoritDetailComponent, AuthoritUpdateComponent, AuthoritDeleteDialogComponent],
  entryComponents: [AuthoritDeleteDialogComponent],
})
export class PolySignAuthoritModule {}
