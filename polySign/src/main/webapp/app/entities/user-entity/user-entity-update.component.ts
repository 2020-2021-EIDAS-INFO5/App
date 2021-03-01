import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IUserEntity, UserEntity } from 'app/shared/model/user-entity.model';
import { UserEntityService } from './user-entity.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-user-entity-update',
  templateUrl: './user-entity-update.component.html',
})
export class UserEntityUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

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
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userEntity }) => {
      this.updateForm(userEntity);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
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

  save(): void {
    this.isSaving = true;
    const userEntity = this.createFromForm();
    if (userEntity.id !== undefined) {
      this.subscribeToSaveResponse(this.userEntityService.update(userEntity));
    } else {
      this.subscribeToSaveResponse(this.userEntityService.create(userEntity));
    }
  }

  private createFromForm(): IUserEntity {
    return {
      ...new UserEntity(),
      id: this.editForm.get(['id'])!.value,
      firstname: this.editForm.get(['firstname'])!.value,
      lastname: this.editForm.get(['lastname'])!.value,
      email: this.editForm.get(['email'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserEntity>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IUser): any {
    return item.id;
  }
}
