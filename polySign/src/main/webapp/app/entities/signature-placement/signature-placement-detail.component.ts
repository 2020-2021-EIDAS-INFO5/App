import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISignaturePlacement } from 'app/shared/model/signature-placement.model';

@Component({
  selector: 'jhi-signature-placement-detail',
  templateUrl: './signature-placement-detail.component.html',
})
export class SignaturePlacementDetailComponent implements OnInit {
  signaturePlacement: ISignaturePlacement | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ signaturePlacement }) => (this.signaturePlacement = signaturePlacement));
  }

  previousState(): void {
    window.history.back();
  }
}
