import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ISignaturePlacement, SignaturePlacement } from 'app/shared/model/signature-placement.model';
import { SignaturePlacementService } from './signature-placement.service';
import { ISignOrder } from 'app/shared/model/sign-order.model';
import { SignOrderService } from 'app/entities/sign-order/sign-order.service';

@Component({
  selector: 'jhi-signature-placement-update',
  templateUrl: './signature-placement-update.component.html',
})
export class SignaturePlacementUpdateComponent implements OnInit {
  isSaving = false;
  signorders: ISignOrder[] = [];

  editForm = this.fb.group({
    id: [],
    pageNumber: [],
    coordinateX: [],
    coordinateY: [],
    placement: [],
  });

  constructor(
    protected signaturePlacementService: SignaturePlacementService,
    protected signOrderService: SignOrderService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ signaturePlacement }) => {
      this.updateForm(signaturePlacement);

      this.signOrderService.query().subscribe((res: HttpResponse<ISignOrder[]>) => (this.signorders = res.body || []));
    });
  }

  updateForm(signaturePlacement: ISignaturePlacement): void {
    this.editForm.patchValue({
      id: signaturePlacement.id,
      pageNumber: signaturePlacement.pageNumber,
      coordinateX: signaturePlacement.coordinateX,
      coordinateY: signaturePlacement.coordinateY,
      placement: signaturePlacement.placement,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const signaturePlacement = this.createFromForm();
    if (signaturePlacement.id !== undefined) {
      this.subscribeToSaveResponse(this.signaturePlacementService.update(signaturePlacement));
    } else {
      this.subscribeToSaveResponse(this.signaturePlacementService.create(signaturePlacement));
    }
  }

  private createFromForm(): ISignaturePlacement {
    return {
      ...new SignaturePlacement(),
      id: this.editForm.get(['id'])!.value,
      pageNumber: this.editForm.get(['pageNumber'])!.value,
      coordinateX: this.editForm.get(['coordinateX'])!.value,
      coordinateY: this.editForm.get(['coordinateY'])!.value,
      placement: this.editForm.get(['placement'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISignaturePlacement>>): void {
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

  trackById(index: number, item: ISignOrder): any {
    return item.id;
  }
}
