import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PolySignSharedModule } from 'app/shared/shared.module';
import { SignatureProcessComponent } from './signature-process.component';
import { SignatureProcessDetailComponent } from './signature-process-detail.component';
import { SignatureProcessUpdateComponent } from './signature-process-update.component';
import { SignatureProcessDeleteDialogComponent } from './signature-process-delete-dialog.component';
import { signatureProcessRoute } from './signature-process.route';
import { SignatureProcessStepOneCreationComponent } from './signature-process-step-one-creation.component';
import { SignatureProcessStepTwoCreationComponent } from './signature-process-step-two-creation.component';

@NgModule({
  imports: [PolySignSharedModule, RouterModule.forChild(signatureProcessRoute)],
  declarations: [
    SignatureProcessComponent,
    SignatureProcessDetailComponent,
    SignatureProcessStepOneCreationComponent,
    SignatureProcessStepTwoCreationComponent,
    SignatureProcessUpdateComponent,
    SignatureProcessDeleteDialogComponent,
  ],
  entryComponents: [SignatureProcessDeleteDialogComponent],
})
export class PolySignSignatureProcessModule {}
