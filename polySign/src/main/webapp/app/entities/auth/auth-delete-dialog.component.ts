import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAuth } from 'app/shared/model/auth.model';
import { AuthService } from './auth.service';

@Component({
  templateUrl: './auth-delete-dialog.component.html',
})
export class AuthDeleteDialogComponent {
  auth?: IAuth;

  constructor(protected authService: AuthService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.authService.delete(id).subscribe(() => {
      this.eventManager.broadcast('authListModification');
      this.activeModal.close();
    });
  }
}
