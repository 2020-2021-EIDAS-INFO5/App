import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PolySignSharedModule } from 'app/shared/shared.module';
import { UserEntityComponent } from './user-entity.component';
import { UserEntityDetailComponent } from './user-entity-detail.component';
import { UserEntityUpdateComponent } from './user-entity-update.component';
import { UserEntityDeleteDialogComponent } from './user-entity-delete-dialog.component';
import { userEntityRoute } from './user-entity.route';
import { UserOrganizationCreateComponent } from './user-organization-create.component';

@NgModule({
  imports: [PolySignSharedModule, RouterModule.forChild(userEntityRoute)],
  declarations: [
    UserEntityComponent,
    UserEntityDetailComponent,
    UserEntityUpdateComponent,
    UserEntityDeleteDialogComponent,
    UserOrganizationCreateComponent,
  ],
  entryComponents: [UserEntityDeleteDialogComponent],
})
export class PolySignUserEntityModule {}
