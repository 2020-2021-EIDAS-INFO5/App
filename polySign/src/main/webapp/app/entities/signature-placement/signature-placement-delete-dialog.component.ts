import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISignaturePlacement } from 'app/shared/model/signature-placement.model';
import { SignaturePlacementService } from './signature-placement.service';

@Component({
  templateUrl: './signature-placement-delete-dialog.component.html',
})
export class SignaturePlacementDeleteDialogComponent {
  signaturePlacement?: ISignaturePlacement;

  constructor(
    protected signaturePlacementService: SignaturePlacementService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.signaturePlacementService.delete(id).subscribe(() => {
      this.eventManager.broadcast('signaturePlacementListModification');
      this.activeModal.close();
    });
  }
}
