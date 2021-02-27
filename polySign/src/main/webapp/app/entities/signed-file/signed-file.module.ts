import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PolySignSharedModule } from 'app/shared/shared.module';
import { SignedFileComponent } from './signed-file.component';
import { SignedFileDetailComponent } from './signed-file-detail.component';
import { SignedFileUpdateComponent } from './signed-file-update.component';
import { SignedFileDeleteDialogComponent } from './signed-file-delete-dialog.component';
import { signedFileRoute } from './signed-file.route';

@NgModule({
  imports: [PolySignSharedModule, RouterModule.forChild(signedFileRoute)],
  declarations: [SignedFileComponent, SignedFileDetailComponent, SignedFileUpdateComponent, SignedFileDeleteDialogComponent],
  entryComponents: [SignedFileDeleteDialogComponent],
})
export class PolySignSignedFileModule {}
