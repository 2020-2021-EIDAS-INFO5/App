import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ISignatureProcess, SignatureProcess } from 'app/shared/model/signature-process.model';
import { SignatureProcessService } from './signature-process.service';
import { IAuthenticatedUser } from 'app/shared/model/authenticated-user.model';
import { AuthenticatedUserService } from 'app/entities/authenticated-user/authenticated-user.service';

@Component({
  selector: 'jhi-signature-process-update',
  templateUrl: './signature-process-update.component.html',
})
export class SignatureProcessUpdateComponent implements OnInit {
  isSaving = false;
  authenticatedusers: IAuthenticatedUser[] = [];

  editForm = this.fb.group({
    id: [],
    emissionDate: [null, [Validators.required]],
    expirationDate: [null, [Validators.required]],
    title: [null, [Validators.required]],
    status: [null, [Validators.required]],
    orderedSigning: [null, [Validators.required]],
    creator: [],
  });

  constructor(
    protected signatureProcessService: SignatureProcessService,
    protected authenticatedUserService: AuthenticatedUserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ signatureProcess }) => {
      if (!signatureProcess.id) {
        const today = moment().startOf('day');
        signatureProcess.emissionDate = today;
        signatureProcess.expirationDate = today;
      }

      this.updateForm(signatureProcess);

      this.authenticatedUserService
        .query()
        .subscribe((res: HttpResponse<IAuthenticatedUser[]>) => (this.authenticatedusers = res.body || []));
    });
  }

  updateForm(signatureProcess: ISignatureProcess): void {
    this.editForm.patchValue({
      id: signatureProcess.id,
      emissionDate: signatureProcess.emissionDate ? signatureProcess.emissionDate.format(DATE_TIME_FORMAT) : null,
      expirationDate: signatureProcess.expirationDate ? signatureProcess.expirationDate.format(DATE_TIME_FORMAT) : null,
      title: signatureProcess.title,
      status: signatureProcess.status,
      orderedSigning: signatureProcess.orderedSigning,
      creator: signatureProcess.creator,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const signatureProcess = this.createFromForm();
    if (signatureProcess.id !== undefined) {
      this.subscribeToSaveResponse(this.signatureProcessService.update(signatureProcess));
    } else {
      this.subscribeToSaveResponse(this.signatureProcessService.create(signatureProcess));
    }
  }

  private createFromForm(): ISignatureProcess {
    return {
      ...new SignatureProcess(),
      id: this.editForm.get(['id'])!.value,
      emissionDate: this.editForm.get(['emissionDate'])!.value
        ? moment(this.editForm.get(['emissionDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      expirationDate: this.editForm.get(['expirationDate'])!.value
        ? moment(this.editForm.get(['expirationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      title: this.editForm.get(['title'])!.value,
      status: this.editForm.get(['status'])!.value,
      orderedSigning: this.editForm.get(['orderedSigning'])!.value,
      creator: this.editForm.get(['creator'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISignatureProcess>>): void {
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

  trackById(index: number, item: IAuthenticatedUser): any {
    return item.id;
  }
}
