import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IAuth, Auth } from 'app/shared/model/auth.model';
import { AuthService } from './auth.service';
import { IAuthenticatedUser } from 'app/shared/model/authenticated-user.model';
import { AuthenticatedUserService } from 'app/entities/authenticated-user/authenticated-user.service';
import { IOrganization } from 'app/shared/model/organization.model';
import { OrganizationService } from 'app/entities/organization/organization.service';

type SelectableEntity = IAuthenticatedUser | IOrganization;

@Component({
  selector: 'jhi-auth-update',
  templateUrl: './auth-update.component.html',
})
export class AuthUpdateComponent implements OnInit {
  isSaving = false;
  authenticatedusers: IAuthenticatedUser[] = [];
  organizations: IOrganization[] = [];

  editForm = this.fb.group({
    id: [],
    hasRole: [null, [Validators.required]],
    authenticatedUser: [],
    organization: [],
  });

  constructor(
    protected authService: AuthService,
    protected authenticatedUserService: AuthenticatedUserService,
    protected organizationService: OrganizationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ auth }) => {
      this.updateForm(auth);

      this.authenticatedUserService
        .query()
        .subscribe((res: HttpResponse<IAuthenticatedUser[]>) => (this.authenticatedusers = res.body || []));

      this.organizationService.query().subscribe((res: HttpResponse<IOrganization[]>) => (this.organizations = res.body || []));
    });
  }

  updateForm(auth: IAuth): void {
    this.editForm.patchValue({
      id: auth.id,
      hasRole: auth.hasRole,
      authenticatedUser: auth.authenticatedUser,
      organization: auth.organization,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const auth = this.createFromForm();
    if (auth.id !== undefined) {
      this.subscribeToSaveResponse(this.authService.update(auth));
    } else {
      this.subscribeToSaveResponse(this.authService.create(auth));
    }
  }

  private createFromForm(): IAuth {
    return {
      ...new Auth(),
      id: this.editForm.get(['id'])!.value,
      hasRole: this.editForm.get(['hasRole'])!.value,
      authenticatedUser: this.editForm.get(['authenticatedUser'])!.value,
      organization: this.editForm.get(['organization'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAuth>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
