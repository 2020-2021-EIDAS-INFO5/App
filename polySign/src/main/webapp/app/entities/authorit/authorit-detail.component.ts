import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAuthorit } from 'app/shared/model/authorit.model';

@Component({
  selector: 'jhi-authorit-detail',
  templateUrl: './authorit-detail.component.html',
})
export class AuthoritDetailComponent implements OnInit {
  authorit: IAuthorit | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ authorit }) => (this.authorit = authorit));
  }

  previousState(): void {
    window.history.back();
  }
}
