import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ISignOrder, SignOrder } from 'app/shared/model/sign-order.model';
import { SignOrderService } from './sign-order.service';
import { ISignedFile } from 'app/shared/model/signed-file.model';
import { SignedFileService } from 'app/entities/signed-file/signed-file.service';
import { IUserEntity } from 'app/shared/model/user-entity.model';
import { UserEntityService } from 'app/entities/user-entity/user-entity.service';
import { ISignatureProcess } from 'app/shared/model/signature-process.model';
import { SignatureProcessService } from 'app/entities/signature-process/signature-process.service';

type SelectableEntity = ISignedFile | IUserEntity | ISignatureProcess;

@Component({
  selector: 'jhi-sign-order-update',
  templateUrl: './sign-order-update.component.html',
})
export class SignOrderUpdateComponent implements OnInit {
  isSaving = false;
  files: ISignedFile[] = [];
  userentities: IUserEntity[] = [];
  signatureprocesses: ISignatureProcess[] = [];

  editForm = this.fb.group({
    id: [],
    rank: [],
    signed: [],
    file: [],
    signer: [],
    signature: [],
  });

  constructor(
    protected signOrderService: SignOrderService,
    protected signedFileService: SignedFileService,
    protected userEntityService: UserEntityService,
    protected signatureProcessService: SignatureProcessService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ signOrder }) => {
      this.updateForm(signOrder);

      this.signedFileService
        .query({ filter: 'signorder-is-null' })
        .pipe(
          map((res: HttpResponse<ISignedFile[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ISignedFile[]) => {
          if (!signOrder.file || !signOrder.file.id) {
            this.files = resBody;
          } else {
            this.signedFileService
              .find(signOrder.file.id)
              .pipe(
                map((subRes: HttpResponse<ISignedFile>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ISignedFile[]) => (this.files = concatRes));
          }
        });

      this.userEntityService.query().subscribe((res: HttpResponse<IUserEntity[]>) => (this.userentities = res.body || []));

      this.signatureProcessService
        .query()
        .subscribe((res: HttpResponse<ISignatureProcess[]>) => (this.signatureprocesses = res.body || []));
    });
  }

  updateForm(signOrder: ISignOrder): void {
    this.editForm.patchValue({
      id: signOrder.id,
      rank: signOrder.rank,
      signed: signOrder.signed,
      file: signOrder.file,
      signer: signOrder.signer,
      signature: signOrder.signature,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const signOrder = this.createFromForm();
    if (signOrder.id !== undefined) {
      this.subscribeToSaveResponse(this.signOrderService.update(signOrder));
    } else {
      this.subscribeToSaveResponse(this.signOrderService.create(signOrder));
    }
  }

  private createFromForm(): ISignOrder {
    return {
      ...new SignOrder(),
      id: this.editForm.get(['id'])!.value,
      rank: this.editForm.get(['rank'])!.value,
      signed: this.editForm.get(['signed'])!.value,
      file: this.editForm.get(['file'])!.value,
      signer: this.editForm.get(['signer'])!.value,
      signature: this.editForm.get(['signature'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISignOrder>>): void {
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