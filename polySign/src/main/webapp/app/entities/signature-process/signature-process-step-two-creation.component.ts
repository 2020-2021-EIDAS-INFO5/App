import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable, Subscription } from 'rxjs';

import { IUserEntity, UserEntity } from 'app/shared/model/user-entity.model';
import { UserEntityService } from '../user-entity/user-entity.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { JhiEventManager } from 'ng-jhipster';
import { Account } from 'app/core/user/account.model';
import { AccountService } from '../../core/auth/account.service';
import { SignOrderService } from '../sign-order/sign-order.service';
import { ISignOrder } from '../../shared/model/sign-order.model';

@Component({
  selector: 'jhi-signature-process-step-two-creation',
  templateUrl: './signature-process-step-two-creation.html',
  styleUrls: ['./signature-process-step-two-creation.scss'],
})
export class SignatureProcessStepTwoCreationComponent implements OnInit, OnDestroy {
  isSaving = false;
  showUserForm = true;
  showRoleForm = false;
  eventSubscriber?: Subscription;
  signers: IUserEntity[] = [];
  users: IUser[] = [];
  userEntities: IUserEntity[] = [];
  /*
  userEntities$?: Observable<IUserEntity[]>;
  filter = new FormControl('');*/

  @Input() signedFileID?: number;
  @Input() organisationID?: number;

  // @Output() showForm = new EventEmitter<{ showUserForm: boolean; showRoleForm: boolean }>();

  account?: Account | null;

  editForm = this.fb.group({
    id: [],
    firstname: [null, [Validators.required]],
    lastname: [null, [Validators.required]],
    email: [null, [Validators.required, Validators.pattern('^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$')]],
    phone: [null, [Validators.required, Validators.maxLength(15)]],
    user: [],
  });

  constructor(
    protected userEntityService: UserEntityService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private signOrderService: SignOrderService,
    private fb: FormBuilder,
    private accountService: AccountService,
    private eventManager: JhiEventManager // pipe: DecimalPipe
  ) {}

  ngOnInit(): void {
    /* this.activatedRoute.data.subscribe(({ userEntity }) => {
      this.updateForm(userEntity);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });*/
    // this.userEntityService.query().subscribe((res: HttpResponse<IUserEntity[]>) => (this.userEntities = res.body || []));
    this.accountService.identity().subscribe(account => (this.account = account));
  }

  updateForm(userEntity: IUserEntity): void {
    this.editForm.patchValue({
      id: userEntity.id,
      firstname: userEntity.firstname,
      lastname: userEntity.lastname,
      email: userEntity.email,
      phone: userEntity.phone,
      user: userEntity.user,
    });
  }

  previousState(): void {
    window.history.back();
  }

  registerChangeInUserEntities(): void {
    this.eventSubscriber = this.eventManager.subscribe('userEntityListModification', () => {});
  }

  save(): void {
    this.isSaving = true;
    const userEntity = this.createFromForm();
    /* eslint-disable no-console */
    console.log(userEntity);

    if (userEntity.id !== undefined && userEntity.id !== null) {
      this.subscribeToSaveResponse(this.userEntityService.update(userEntity));
    } else {
      this.subscribeToSaveResponse(
        this.signOrderService.createSignOrderForUserEntity(userEntity, this.signedFileID!, this.organisationID!)
      );
    }

    // this.showForm();
  }

  private createFromForm(): IUserEntity {
    return {
      ...new UserEntity(),
      id: this.editForm.get(['id'])!.value,
      firstname: this.editForm.get(['firstname'])!.value,
      lastname: this.editForm.get(['lastname'])!.value,
      email: this.editForm.get(['email'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      user: undefined,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISignOrder>>): void {
    result.subscribe(
      res => {
        // we retrieve the userEntity that has been created and we put it the signers array
        this.signers.push(res.body!.signer!);
        this.onSaveSuccess();
      },
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.editForm.reset();
    // retrieve the array of users after we added a user with the user creation step
    /* this.userEntityService.query().subscribe((res: HttpResponse<IUserEntity[]>) => {
      this.userEntities = res.body || [];
      this.userEntities = this.userEntities.filter(
        u => u.firstname === this.editForm.get(['firstname'])!.value && u.phone === this.editForm.get(['phone'])!.value
      );
    });*/
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  /* trackById(index: number, item: IUser): any {
    return item.id;
  } */

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackById(index: number, item: IUserEntity): any {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id;
  }

  delete(userEntity: IUserEntity): void {
    this.userEntityService.deleteUserEntityByUsername(userEntity.id!, this.account!.email).subscribe(() => {
      this.eventManager.broadcast('userEntityListModification');
    });

    this.signers = this.signers.filter(signer => signer.id !== userEntity.id);
  }
  // we processing the passing from step 2 to step 3
  next(): void {}
  /*
  search(text: string, pipe: PipeTransform): IUserEntity[] {
    return this.userEntities.filter(user => {
      const term = text.toLowerCase();
      return user.firstname!.toLowerCase().includes(term)
        || pipe.transform(user.lastname).includes(term)
        || pipe.transform(user.email).includes(term);
    });
  }*/
}
