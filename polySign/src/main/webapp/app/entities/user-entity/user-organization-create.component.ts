import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, NavigationStart, Event as NavigationEvent, Router, RouterEvent } from '@angular/router';
import { UserEntityService } from './user-entity.service';
import { UserService } from '../../core/user/user.service';
import { Authorit, IAuthorit } from '../../shared/model/authorit.model';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthoritService } from '../authorit/authorit.service';
import { IOrganization } from '../../shared/model/organization.model';
import { IUserEntity } from '../../shared/model/user-entity.model';
import { OrganizationService } from '../organization/organization.service';
import { AccountService } from '../../core/auth/account.service';
import { JhiEventManager } from 'ng-jhipster';
import { filter } from 'rxjs/operators';

type SelectableEntity = IOrganization | IUserEntity;

@Component({
  selector: 'jhi-user-organization-create',
  templateUrl: './user-organization-create.component.html',
})
export class UserOrganizationCreateComponent implements OnInit {
  @Input() userEntities: IUserEntity[];

  @Output() showForm = new EventEmitter<{ showUserForm: boolean; showRoleForm: boolean }>();

  isSaving = false;
  organizations: IOrganization[] = [];
  CurrentUser?: Account | null;
  editForm = this.fb.group({
    id: [],
    hasRole: [null, [Validators.required]],
    organization: [],
    user: [],
  });

  constructor(
    protected userEntityService: UserEntityService,
    protected userService: UserService,
    private authoritService: AuthoritService,
    private organizationService: OrganizationService,
    protected activatedRoute: ActivatedRoute,
    private eventManager: JhiEventManager,
    private fb: FormBuilder,
    private accountService: AccountService,
    private router: Router
  ) {
    this.userEntities = [];
    /* this.router.events.pipe(
      filter( (event : NavigationEvent) =>
        event instanceof NavigationStart

     )
    ).subscribe((event : NavigationEvent) => {
      // eslint-disable-next-line no-console
      console.log(" Il y a eu un changement de page constructeur");
      // eslint-disable-next-line no-console
      console.log(event);
    })*/
  }

  ngOnInit(): void {
    this.organizationService.query().subscribe((res: HttpResponse<IOrganization[]>) => (this.organizations = res.body || []));
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
    if (this.userEntities) {
      this.userEntityService.delete(this.userEntities[0].id!).subscribe(() => {
        this.eventManager.broadcast('userEntityListModification');
      });
    }

    window.history.back();
  }

  previousAction(): void {
    this.showForm.emit({ showUserForm: true, showRoleForm: false });
    if (this.userEntities) {
      this.userEntityService.delete(this.userEntities[0].id!).subscribe(() => {
        this.eventManager.broadcast('userEntityListModification');
      });
    }
  }

  save(): void {
    this.isSaving = true;
    const authorit = this.createFromForm();

    if (authorit.id !== undefined && authorit.id !== null) {
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
    window.history.back();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
