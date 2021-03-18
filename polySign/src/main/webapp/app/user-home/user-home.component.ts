import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISignatureProcess } from 'app/shared/model/signature-process.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { SignatureProcessService } from 'app/entities/signature-process/signature-process.service';
import { SignatureProcessDeleteDialogComponent } from 'app/entities/signature-process/signature-process-delete-dialog.component';

@Component({
  selector: 'jhi-user-home',
  templateUrl: './user-home.html',
  styleUrls: ['./user-home.component.scss'],
})
export class UserHomeComponent implements OnInit, OnDestroy {
  signatureProcesses?: ISignatureProcess[] = [];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected signatureProcessService: SignatureProcessService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}
  /*
  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    this.signatureProcessService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<ISignatureProcess[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );
  } */

  ngOnInit(): void {
    this.retrieveAllSignatureProcess();
    this.registerChangeInSignatureProcesses();
    // this.handleNavigation();
    // this.registerChangeInSignatureProcesses();
    // console.log("1");
    // console.log(this.signatureProcesses);
  }
  /*
  protected handleNavigation(): void {
    combineLatest(this.activatedRoute.data, this.activatedRoute.queryParamMap, (data: Data, params: ParamMap) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'asc';
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    }).subscribe();
  }*/

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ISignatureProcess): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInSignatureProcesses(): void {
    this.eventSubscriber = this.eventManager.subscribe('signatureProcessListModification', () => this.retrieveAllSignatureProcess());
  }

  delete(signatureProcess: ISignatureProcess): void {
    const modalRef = this.modalService.open(SignatureProcessDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.signatureProcess = signatureProcess;
  }

  retrieveAllSignatureProcess(): void {
    this.signatureProcessService.query().subscribe((res: HttpResponse<ISignatureProcess[]>) => (this.signatureProcesses = res.body || []));
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }
  /*
  protected onSuccess(data: ISignatureProcess[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/signature-process'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.signatureProcesses = data || [];
    this.ngbPaginationPage = this.page;

    // console.log("3");
    // console.log(this.signatureProcesses);
  } */

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
