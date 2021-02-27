import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAuthenticatedUser } from 'app/shared/model/authenticated-user.model';

@Component({
  selector: 'jhi-authenticated-user-detail',
  templateUrl: './authenticated-user-detail.component.html',
})
export class AuthenticatedUserDetailComponent implements OnInit {
  authenticatedUser: IAuthenticatedUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ authenticatedUser }) => (this.authenticatedUser = authenticatedUser));
  }

  previousState(): void {
    window.history.back();
  }
}
