import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUserEntity } from 'app/shared/model/user-entity.model';
import { UserEntityService } from './user-entity.service';

@Component({
  templateUrl: './user-entity-delete-dialog.component.html',
})
export class UserEntityDeleteDialogComponent {
  userEntity?: IUserEntity;

  constructor(
    protected userEntityService: UserEntityService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userEntityService.delete(id).subscribe(() => {
      this.eventManager.broadcast('userEntityListModification');
      this.activeModal.close();
    });
  }
}
