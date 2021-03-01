import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IAuthorit, Authorit } from 'app/shared/model/authorit.model';
import { AuthoritService } from './authorit.service';
import { IOrganization } from 'app/shared/model/organization.model';
import { OrganizationService } from 'app/entities/organization/organization.service';
import { IUserEntity } from 'app/shared/model/user-entity.model';
import { UserEntityService } from 'app/entities/user-entity/user-entity.service';

type SelectableEntity = IOrganization | IUserEntity;

@Component({
  selector: 'jhi-authorit-update',
  templateUrl: './authorit-update.component.html',
})
export class AuthoritUpdateComponent implements OnInit {
  isSaving = false;
  organizations: IOrganization[] = [];
  userentities: IUserEntity[] = [];

  editForm = this.fb.group({
    id: [],
    hasRole: [null, [Validators.required]],
    organization: [],
    user: [],
  });

  constructor(
    protected authoritService: AuthoritService,
    protected organizationService: OrganizationService,
    protected userEntityService: UserEntityService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ authorit }) => {
      this.updateForm(authorit);

      this.organizationService.query().subscribe((res: HttpResponse<IOrganization[]>) => (this.organizations = res.body || []));

      this.userEntityService.query().subscribe((res: HttpResponse<IUserEntity[]>) => (this.userentities = res.body || []));
    });
  }

  updateForm(authorit: IAuthorit): void {
    this.editForm.patchValue({
      id: authorit.id,
      hasRole: authorit.hasRole,
      organization: authorit.organization,
      user: authorit.user,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const authorit = this.createFromForm();
    if (authorit.id !== undefined) {
      this.subscribeToSaveResponse(this.authoritService.update(authorit));
    } else {
      this.subscribeToSaveResponse(this.authoritService.create(authorit));
    }
  }

  private createFromForm(): IAuthorit {
    return {
      ...new Authorit(),
      id: this.editForm.get(['id'])!.value,
      hasRole: this.editForm.get(['hasRole'])!.value,
      organization: this.editForm.get(['organization'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAuthorit>>): void {
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
