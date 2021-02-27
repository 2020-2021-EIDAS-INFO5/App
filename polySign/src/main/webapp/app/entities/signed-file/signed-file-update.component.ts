import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { ISignedFile, SignedFile } from 'app/shared/model/signed-file.model';
import { SignedFileService } from './signed-file.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { ISignatureProcess } from 'app/shared/model/signature-process.model';
import { SignatureProcessService } from 'app/entities/signature-process/signature-process.service';

@Component({
  selector: 'jhi-signed-file-update',
  templateUrl: './signed-file-update.component.html',
})
export class SignedFileUpdateComponent implements OnInit {
  isSaving = false;
  signatureprocesses: ISignatureProcess[] = [];

  editForm = this.fb.group({
    id: [],
    filename: [null, [Validators.required]],
    fileBytes: [],
    fileBytesContentType: [],
    signingDate: [null, [Validators.required]],
    size: [],
    signature: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected signedFileService: SignedFileService,
    protected signatureProcessService: SignatureProcessService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ signedFile }) => {
      if (!signedFile.id) {
        const today = moment().startOf('day');
        signedFile.signingDate = today;
      }

      this.updateForm(signedFile);

      this.signatureProcessService
        .query()
        .subscribe((res: HttpResponse<ISignatureProcess[]>) => (this.signatureprocesses = res.body || []));
    });
  }

  updateForm(signedFile: ISignedFile): void {
    this.editForm.patchValue({
      id: signedFile.id,
      filename: signedFile.filename,
      fileBytes: signedFile.fileBytes,
      fileBytesContentType: signedFile.fileBytesContentType,
      signingDate: signedFile.signingDate ? signedFile.signingDate.format(DATE_TIME_FORMAT) : null,
      size: signedFile.size,
      signature: signedFile.signature,
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: any, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('polySignApp.error', { ...err, key: 'error.file.' + err.key })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const signedFile = this.createFromForm();
    if (signedFile.id !== undefined) {
      this.subscribeToSaveResponse(this.signedFileService.update(signedFile));
    } else {
      this.subscribeToSaveResponse(this.signedFileService.create(signedFile));
    }
  }

  private createFromForm(): ISignedFile {
    return {
      ...new SignedFile(),
      id: this.editForm.get(['id'])!.value,
      filename: this.editForm.get(['filename'])!.value,
      fileBytesContentType: this.editForm.get(['fileBytesContentType'])!.value,
      fileBytes: this.editForm.get(['fileBytes'])!.value,
      signingDate: this.editForm.get(['signingDate'])!.value
        ? moment(this.editForm.get(['signingDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      size: this.editForm.get(['size'])!.value,
      signature: this.editForm.get(['signature'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISignedFile>>): void {
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

  trackById(index: number, item: ISignatureProcess): any {
    return item.id;
  }
}
