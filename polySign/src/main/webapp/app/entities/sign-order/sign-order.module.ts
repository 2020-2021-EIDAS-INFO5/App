import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PolySignSharedModule } from 'app/shared/shared.module';
import { SignOrderComponent } from './sign-order.component';
import { SignOrderDetailComponent } from './sign-order-detail.component';
import { SignOrderUpdateComponent } from './sign-order-update.component';
import { SignOrderDeleteDialogComponent } from './sign-order-delete-dialog.component';
import { signOrderRoute } from './sign-order.route';

@NgModule({
  imports: [PolySignSharedModule, RouterModule.forChild(signOrderRoute)],
  declarations: [SignOrderComponent, SignOrderDetailComponent, SignOrderUpdateComponent, SignOrderDeleteDialogComponent],
  entryComponents: [SignOrderDeleteDialogComponent],
})
export class PolySignSignOrderModule {}
