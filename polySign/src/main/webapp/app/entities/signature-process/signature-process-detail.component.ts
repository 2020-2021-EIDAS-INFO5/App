import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISignatureProcess } from 'app/shared/model/signature-process.model';

@Component({
  selector: 'jhi-signature-process-detail',
  templateUrl: './signature-process-detail.component.html',
})
export class SignatureProcessDetailComponent implements OnInit {
  signatureProcess: ISignatureProcess | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ signatureProcess }) => (this.signatureProcess = signatureProcess));
  }

  previousState(): void {
    window.history.back();
  }
}
