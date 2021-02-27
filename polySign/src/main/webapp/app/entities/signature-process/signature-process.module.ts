import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PolySignSharedModule } from 'app/shared/shared.module';
import { SignatureProcessComponent } from './signature-process.component';
import { SignatureProcessDetailComponent } from './signature-process-detail.component';
import { SignatureProcessUpdateComponent } from './signature-process-update.component';
import { SignatureProcessDeleteDialogComponent } from './signature-process-delete-dialog.component';
import { signatureProcessRoute } from './signature-process.route';

@NgModule({
  imports: [PolySignSharedModule, RouterModule.forChild(signatureProcessRoute)],
  declarations: [
    SignatureProcessComponent,
    SignatureProcessDetailComponent,
    SignatureProcessUpdateComponent,
    SignatureProcessDeleteDialogComponent,
  ],
  entryComponents: [SignatureProcessDeleteDialogComponent],
})
export class PolySignSignatureProcessModule {}
