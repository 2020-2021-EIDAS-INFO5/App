import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISignOrder } from 'app/shared/model/sign-order.model';
import { SignOrderService } from './sign-order.service';

@Component({
  templateUrl: './sign-order-delete-dialog.component.html',
})
export class SignOrderDeleteDialogComponent {
  signOrder?: ISignOrder;

  constructor(protected signOrderService: SignOrderService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.signOrderService.delete(id).subscribe(() => {
      this.eventManager.broadcast('signOrderListModification');
      this.activeModal.close();
    });
  }
}
