import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAuthenticatedUser } from 'app/shared/model/authenticated-user.model';
import { AuthenticatedUserService } from './authenticated-user.service';

@Component({
  templateUrl: './authenticated-user-delete-dialog.component.html',
})
export class AuthenticatedUserDeleteDialogComponent {
  authenticatedUser?: IAuthenticatedUser;

  constructor(
    protected authenticatedUserService: AuthenticatedUserService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.authenticatedUserService.delete(id).subscribe(() => {
      this.eventManager.broadcast('authenticatedUserListModification');
      this.activeModal.close();
    });
  }
}
