import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PolySignSharedModule } from 'app/shared/shared.module';
import { UserEntityComponent } from './user-entity.component';
import { UserEntityDetailComponent } from './user-entity-detail.component';
import { UserEntityUpdateComponent } from './user-entity-update.component';
import { UserEntityDeleteDialogComponent } from './user-entity-delete-dialog.component';
import { userEntityRoute } from './user-entity.route';

@NgModule({
  imports: [PolySignSharedModule, RouterModule.forChild(userEntityRoute)],
  declarations: [UserEntityComponent, UserEntityDetailComponent, UserEntityUpdateComponent, UserEntityDeleteDialogComponent],
  entryComponents: [UserEntityDeleteDialogComponent],
})
export class PolySignUserEntityModule {}
