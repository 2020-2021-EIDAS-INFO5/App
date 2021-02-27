import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAuth } from 'app/shared/model/auth.model';
import { AuthService } from './auth.service';
import { AuthDeleteDialogComponent } from './auth-delete-dialog.component';

@Component({
  selector: 'jhi-auth',
  templateUrl: './auth.component.html',
})
export class AuthComponent implements OnInit, OnDestroy {
  auths?: IAuth[];
  eventSubscriber?: Subscription;

  constructor(protected authService: AuthService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.authService.query().subscribe((res: HttpResponse<IAuth[]>) => (this.auths = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInAuths();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IAuth): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInAuths(): void {
    this.eventSubscriber = this.eventManager.subscribe('authListModification', () => this.loadAll());
  }

  delete(auth: IAuth): void {
    const modalRef = this.modalService.open(AuthDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.auth = auth;
  }
}
