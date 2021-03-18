import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISignaturePlacement } from 'app/shared/model/signature-placement.model';
import { SignaturePlacementService } from './signature-placement.service';
import { SignaturePlacementDeleteDialogComponent } from './signature-placement-delete-dialog.component';

@Component({
  selector: 'jhi-signature-placement',
  templateUrl: './signature-placement.component.html',
})
export class SignaturePlacementComponent implements OnInit, OnDestroy {
  signaturePlacements?: ISignaturePlacement[];
  eventSubscriber?: Subscription;

  constructor(
    protected signaturePlacementService: SignaturePlacementService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.signaturePlacementService
      .query()
      .subscribe((res: HttpResponse<ISignaturePlacement[]>) => (this.signaturePlacements = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInSignaturePlacements();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ISignaturePlacement): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInSignaturePlacements(): void {
    this.eventSubscriber = this.eventManager.subscribe('signaturePlacementListModification', () => this.loadAll());
  }

  delete(signaturePlacement: ISignaturePlacement): void {
    const modalRef = this.modalService.open(SignaturePlacementDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.signaturePlacement = signaturePlacement;
  }
}
