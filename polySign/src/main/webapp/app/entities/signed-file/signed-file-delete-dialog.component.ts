import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISignedFile } from 'app/shared/model/signed-file.model';
import { SignedFileService } from './signed-file.service';

@Component({
  templateUrl: './signed-file-delete-dialog.component.html',
})
export class SignedFileDeleteDialogComponent {
  signedFile?: ISignedFile;

  constructor(
    protected signedFileService: SignedFileService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.signedFileService.delete(id).subscribe(() => {
      this.eventManager.broadcast('signedFileListModification');
      this.activeModal.close();
    });
  }
}
