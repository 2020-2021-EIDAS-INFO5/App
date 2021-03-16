import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUserEntity } from 'app/shared/model/user-entity.model';
import { UserEntityService } from './user-entity.service';
import { AccountService } from '../../core/auth/account.service';
import { Account } from 'app/core/user/account.model';

@Component({
  templateUrl: './user-entity-delete-dialog.component.html',
})
export class UserEntityDeleteDialogComponent {
  userEntity?: IUserEntity;
  account?: Account | null;

  constructor(
    protected userEntityService: UserEntityService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager,
    private accountService: AccountService
  ) {
    this.accountService.identity().subscribe(account => (this.account = account));
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number, username: string): void {
    this.userEntityService.deleteUserEntityByUsername(id, username).subscribe(() => {
      this.eventManager.broadcast('userEntityListModification');
      this.activeModal.close();
    });
  }
}
