import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable, Subscription } from 'rxjs';

import { IUserEntity, UserEntity } from 'app/shared/model/user-entity.model';
import { UserEntityService } from './user-entity.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { Authorit, IAuthorit } from '../../shared/model/authorit.model';
import { AuthoritService } from '../authorit/authorit.service';
import { IOrganization } from '../../shared/model/organization.model';
import { OrganizationService } from '../organization/organization.service';
import { JhiEventManager } from 'ng-jhipster';

@Component({
  selector: 'jhi-user-entity-update',
  templateUrl: './user-entity-update.component.html',
})
export class UserEntityUpdateComponent implements OnInit, OnDestroy {
  isSaving = false;
  isSavingRole = false;
  showUserForm = true;
  showRoleForm = false;
  // userEntitiesSubscription? : Subscription;
  eventSubscriber?: Subscription;
  users: IUser[] = [];
  // organizations: IOrganization[] = [];
  userEntities: IUserEntity[] = [];

  editForm = this.fb.group({
    id: [],
    firstname: [null, [Validators.required]],
    lastname: [null, [Validators.required]],
    email: [null, [Validators.required, Validators.pattern('^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$')]],
    phone: [null, [Validators.required, Validators.maxLength(15)]],
    user: [],
  });
  /*
  editFormRole = this.fbRole.group({
    idRole: [],
    hasRole: [null, [Validators.required]],
    organization: [],
    user: [],
  });*/

  constructor(
    protected userEntityService: UserEntityService,
    protected userService: UserService,
    // private organizationService: OrganizationService,
    private authoritService: AuthoritService,
    protected activatedRoute: ActivatedRoute,
    // protected activatedRouteRole: ActivatedRoute,
    private fb: FormBuilder,
    // private fbRole: FormBuilder,
    private eventManager: JhiEventManager
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userEntity }) => {
      this.updateForm(userEntity);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
    /*
    this.organizationService.query().subscribe((res: HttpResponse<IOrganization[]>) => (this.organizations = res.body || []));

    this.userEntitiesSubscription = this.userEntityService.userEntitiesSubject.subscribe((users : IUserEntity[]) => {
      this.userEntities = users;
    });
    this.userEntityService.EmitUserEntitiesSubject();*/

    // this.retrieveAll();

    //this.registerChangeInUserEntities();
  }

  retrieveAll(): void {
    // this.organizationService.query().subscribe((res: HttpResponse<IOrganization[]>) => (this.organizations = res.body || []));
    // this.userEntityService.query().subscribe((res: HttpResponse<IUserEntity[]>) => (this.userEntities = res.body || []));
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
  /*
  updateFormRole(authorit: IAuthorit): void {
    this.editForm.patchValue({
      id: authorit.id,
      hasRole: authorit.hasRole,
      organization: authorit.organization,
      user: authorit.user,
    });
  }*/

  previousState(): void {
    window.history.back();
  }
  /*
  previousStateRole(): void {
    if(this.userEntities.length===1) {
      if(this.userEntities[0].id !== undefined){
        this.userEntityService.delete(this.userEntities[0].id).subscribe( () => {
            this.eventManager.broadcast('userEntityListModification');
          }
        );
      }
    }
    window.history.back();
  }*/

  registerChangeInUserEntities(): void {
    this.eventSubscriber = this.eventManager.subscribe('userEntityListModification', () => {
      // eslint-disable-next-line no-console
      console.log('userEntityListModification called');
    });
  }

  save(): void {
    this.isSaving = true;
    const userEntity = this.createFromForm();

    if (userEntity.id !== undefined) {
      this.subscribeToSaveResponse(this.userEntityService.update(userEntity));
    } else {
      // eslint-disable-next-line no-console
      // console.log(userEntity.id);
      this.subscribeToSaveResponse(this.userEntityService.create(userEntity));
      // this.userEntityService.EmitUserEntitiesSubject();
    }

    this.showForm();
  }
  /*
  saveRole(): void {
    this.isSavingRole = true;
    const authorit = this.createFromFormRole();
    if (authorit.id !== undefined) {
      this.subscribeToSaveResponseRole(this.authoritService.update(authorit));
    } else {
      this.subscribeToSaveResponseRole(this.authoritService.create(authorit));
    }
  }*/

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
  /*
  private createFromFormRole(): IAuthorit {
    return {
      ...new Authorit(),
      id: this.editFormRole.get(['idRole'])!.value,
      hasRole: this.editFormRole.get(['hasRole'])!.value,
      organization: this.editFormRole.get(['organization'])!.value,
      user: this.editFormRole.get(['user'])!.value,
    };
  }*/

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserEntity>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }
  /*
  protected subscribeToSaveResponseRole(result: Observable<HttpResponse<IAuthorit>>): void {
    result.subscribe(
      () => this.onSaveSuccessRole(),
      () => this.onSaveErrorRole()
    );
  }*/

  protected onSaveSuccess(): void {
    this.isSaving = false;
    // this.previousState();
    // retrieve the array of users after we added a user with the user creation step
    this.userEntityService.query().subscribe((res: HttpResponse<IUserEntity[]>) => {
      this.userEntities = res.body || [];
      // eslint-disable-next-line no-console
      console.log(this.userEntities);
      this.userEntities = this.userEntities.filter(
        u => u.firstname === this.editForm.get(['firstname'])!.value && u.phone === this.editForm.get(['phone'])!.value
      );
      // eslint-disable-next-line no-console
      console.log(this.userEntities);
    });
  }
  /*
  protected onSaveSuccessRole(): void {
    this.isSavingRole = false;
    this.previousState();
  }*/

  protected onSaveError(): void {
    this.isSaving = false;
  }
  /*
  protected onSaveErrorRole(): void {
    this.isSavingRole = false;
  }*/

  /* trackById(index: number, item: IUser): any {
    return item.id;
  } */

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
    // this.userEntitiesSubscription?.unsubscribe();
  }

  trackById(index: number, item: IUser): any {
    return item.id;
  }

  showForm(): void {
    this.showUserForm = !this.showUserForm;
    this.showRoleForm = !this.showRoleForm;
  }

  onShowForm(showForm: { showUserForm: boolean; showRoleForm: boolean }): void {
    this.showUserForm = showForm.showUserForm;
    this.showRoleForm = showForm.showRoleForm;
  }
  /*
  showFormRole() : void {
    this.showUserForm = !this.showUserForm;
    this.showRoleForm = !this.showRoleForm;
    if(this.userEntities[0].id !== undefined){
        this.userEntityService.delete(this.userEntities[0].id).subscribe( () => {
            this.eventManager.broadcast('userEntityListModification');
          }
        );
    }
  }*/
}
