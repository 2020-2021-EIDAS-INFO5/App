import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IAuthenticatedUser, AuthenticatedUser } from 'app/shared/model/authenticated-user.model';
import { AuthenticatedUserService } from './authenticated-user.service';
import { IUserEntity } from 'app/shared/model/user-entity.model';
import { UserEntityService } from 'app/entities/user-entity/user-entity.service';

@Component({
  selector: 'jhi-authenticated-user-update',
  templateUrl: './authenticated-user-update.component.html',
})
export class AuthenticatedUserUpdateComponent implements OnInit {
  isSaving = false;
  users: IUserEntity[] = [];

  editForm = this.fb.group({
    id: [],
    user: [],
  });

  constructor(
    protected authenticatedUserService: AuthenticatedUserService,
    protected userEntityService: UserEntityService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ authenticatedUser }) => {
      this.updateForm(authenticatedUser);

      this.userEntityService
        .query({ filter: 'authenticateduser-is-null' })
        .pipe(
          map((res: HttpResponse<IUserEntity[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IUserEntity[]) => {
          if (!authenticatedUser.user || !authenticatedUser.user.id) {
            this.users = resBody;
          } else {
            this.userEntityService
              .find(authenticatedUser.user.id)
              .pipe(
                map((subRes: HttpResponse<IUserEntity>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IUserEntity[]) => (this.users = concatRes));
          }
        });
    });
  }

  updateForm(authenticatedUser: IAuthenticatedUser): void {
    this.editForm.patchValue({
      id: authenticatedUser.id,
      user: authenticatedUser.user,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const authenticatedUser = this.createFromForm();
    if (authenticatedUser.id !== undefined) {
      this.subscribeToSaveResponse(this.authenticatedUserService.update(authenticatedUser));
    } else {
      this.subscribeToSaveResponse(this.authenticatedUserService.create(authenticatedUser));
    }
  }

  private createFromForm(): IAuthenticatedUser {
    return {
      ...new AuthenticatedUser(),
      id: this.editForm.get(['id'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAuthenticatedUser>>): void {
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

  trackById(index: number, item: IUserEntity): any {
    return item.id;
  }
}
