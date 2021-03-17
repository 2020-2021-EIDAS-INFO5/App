import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PolySignSharedModule } from 'app/shared/shared.module';
import { SignaturePlacementComponent } from './signature-placement.component';
import { SignaturePlacementDetailComponent } from './signature-placement-detail.component';
import { SignaturePlacementUpdateComponent } from './signature-placement-update.component';
import { SignaturePlacementDeleteDialogComponent } from './signature-placement-delete-dialog.component';
import { signaturePlacementRoute } from './signature-placement.route';

@NgModule({
  imports: [PolySignSharedModule, RouterModule.forChild(signaturePlacementRoute)],
  declarations: [
    SignaturePlacementComponent,
    SignaturePlacementDetailComponent,
    SignaturePlacementUpdateComponent,
    SignaturePlacementDeleteDialogComponent,
  ],
  entryComponents: [SignaturePlacementDeleteDialogComponent],
})
export class PolySignSignaturePlacementModule {}
