import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISignatureProcess } from 'app/shared/model/signature-process.model';
import { SignatureProcessService } from './signature-process.service';

@Component({
  templateUrl: './signature-process-delete-dialog.component.html',
})
export class SignatureProcessDeleteDialogComponent {
  signatureProcess?: ISignatureProcess;

  constructor(
    protected signatureProcessService: SignatureProcessService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.signatureProcessService.delete(id).subscribe(() => {
      this.eventManager.broadcast('signatureProcessListModification');
      this.activeModal.close();
    });
  }
}
