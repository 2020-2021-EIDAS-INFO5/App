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
import { SignedFileService } from '../signed-file/signed-file.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { AccountService } from '../../core/auth/account.service';
import { Account } from 'app/core/user/account.model';

@Component({
  selector: 'jhi-signature-process-step-one-creation',
  templateUrl: './signature-process-step-one-creation.html',
  styleUrls: ['./signature-process-step-one-creation.scss'],
})
export class SignatureProcessStepOneCreationComponent implements OnInit {
  isSaving = false;

  signedFileCreated?: ISignedFile;
  protected account?: Account | null;

  editForm = this.fb.group({
    id: [],
    filename: [null, [Validators.required]],
    fileBytes: [],
    fileBytesContentType: [],
    signingDate: [null, [Validators.required]],
    size: [],
    sha256: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected signedFileService: SignedFileService,
    protected activatedRoute: ActivatedRoute,
    protected accountService: AccountService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ signedFile }) => {
      if (!signedFile.id) {
        const today = moment().startOf('day');
        signedFile.signingDate = today;
      }

      this.updateForm(signedFile);
      this.accountService.identity().subscribe(account => (this.account = account));
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
      sha256: signedFile.sha256,
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
    // eslint-disable-next-line no-console
    console.log(signedFile);
    if (signedFile.id !== undefined && signedFile.id !== null) {
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
      size: undefined,
      sha256: undefined,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISignedFile>>): void {
    result.subscribe(
      res => {
        this.signedFileCreated = res.body || undefined;
        this.onSaveSuccess();
      },
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
}
