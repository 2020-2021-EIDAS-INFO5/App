import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAuthorit } from 'app/shared/model/authorit.model';
import { AuthoritService } from './authorit.service';

@Component({
  templateUrl: './authorit-delete-dialog.component.html',
})
export class AuthoritDeleteDialogComponent {
  authorit?: IAuthorit;

  constructor(protected authoritService: AuthoritService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.authoritService.delete(id).subscribe(() => {
      this.eventManager.broadcast('authoritListModification');
      this.activeModal.close();
    });
  }
}
