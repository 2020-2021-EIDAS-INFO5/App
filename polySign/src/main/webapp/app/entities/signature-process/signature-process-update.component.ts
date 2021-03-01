import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ISignatureProcess, SignatureProcess } from 'app/shared/model/signature-process.model';
import { SignatureProcessService } from './signature-process.service';
import { ISignedFile } from 'app/shared/model/signed-file.model';
import { SignedFileService } from 'app/entities/signed-file/signed-file.service';
import { IUserEntity } from 'app/shared/model/user-entity.model';
import { UserEntityService } from 'app/entities/user-entity/user-entity.service';

type SelectableEntity = ISignedFile | IUserEntity;

@Component({
  selector: 'jhi-signature-process-update',
  templateUrl: './signature-process-update.component.html',
})
export class SignatureProcessUpdateComponent implements OnInit {
  isSaving = false;
  finalfiles: ISignedFile[] = [];
  userentities: IUserEntity[] = [];

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required]],
    emissionDate: [null, [Validators.required]],
    expirationDate: [null, [Validators.required]],
    status: [null, [Validators.required]],
    orderedSigning: [null, [Validators.required]],
    finalFile: [],
    creator: [],
  });

  constructor(
    protected signatureProcessService: SignatureProcessService,
    protected signedFileService: SignedFileService,
    protected userEntityService: UserEntityService,
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

      this.signedFileService
        .query({ filter: 'signatureprocess-is-null' })
        .pipe(
          map((res: HttpResponse<ISignedFile[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ISignedFile[]) => {
          if (!signatureProcess.finalFile || !signatureProcess.finalFile.id) {
            this.finalfiles = resBody;
          } else {
            this.signedFileService
              .find(signatureProcess.finalFile.id)
              .pipe(
                map((subRes: HttpResponse<ISignedFile>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ISignedFile[]) => (this.finalfiles = concatRes));
          }
        });

      this.userEntityService.query().subscribe((res: HttpResponse<IUserEntity[]>) => (this.userentities = res.body || []));
    });
  }

  updateForm(signatureProcess: ISignatureProcess): void {
    this.editForm.patchValue({
      id: signatureProcess.id,
      title: signatureProcess.title,
      emissionDate: signatureProcess.emissionDate ? signatureProcess.emissionDate.format(DATE_TIME_FORMAT) : null,
      expirationDate: signatureProcess.expirationDate ? signatureProcess.expirationDate.format(DATE_TIME_FORMAT) : null,
      status: signatureProcess.status,
      orderedSigning: signatureProcess.orderedSigning,
      finalFile: signatureProcess.finalFile,
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
      title: this.editForm.get(['title'])!.value,
      emissionDate: this.editForm.get(['emissionDate'])!.value
        ? moment(this.editForm.get(['emissionDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      expirationDate: this.editForm.get(['expirationDate'])!.value
        ? moment(this.editForm.get(['expirationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      status: this.editForm.get(['status'])!.value,
      orderedSigning: this.editForm.get(['orderedSigning'])!.value,
      finalFile: this.editForm.get(['finalFile'])!.value,
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
