import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserEntity } from 'app/shared/model/user-entity.model';

@Component({
  selector: 'jhi-user-entity-detail',
  templateUrl: './user-entity-detail.component.html',
})
export class UserEntityDetailComponent implements OnInit {
  userEntity: IUserEntity | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userEntity }) => (this.userEntity = userEntity));
  }

  previousState(): void {
    window.history.back();
  }
}
