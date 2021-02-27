import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISignOrder } from 'app/shared/model/sign-order.model';

@Component({
  selector: 'jhi-sign-order-detail',
  templateUrl: './sign-order-detail.component.html',
})
export class SignOrderDetailComponent implements OnInit {
  signOrder: ISignOrder | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ signOrder }) => (this.signOrder = signOrder));
  }

  previousState(): void {
    window.history.back();
  }
}
