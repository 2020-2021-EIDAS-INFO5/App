import { Component, OnInit, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';
import { LoginService } from 'app/core/login/login.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { ISignedFile } from 'app/shared/model/signed-file.model';
import { CdkDragEnd, CdkDragStart, CdkDragMove } from '@angular/cdk/drag-drop';
import * as pdfjsLib from 'pdfjs-dist';
if (pdfjsLib !== undefined) {
  pdfjsLib.GlobalWorkerOptions.workerSrc = 'https://npmcdn.com/pdfjs-dist@2.6.347/build/pdf.worker.js';
}

@Component({
  selector: 'jhi-signed-file-detail',
  templateUrl: './signed-file-detail.component.html',
  styleUrls: ['./signed-file-details.scss'],
})
export class SignedFileDetailComponent implements OnInit, AfterViewInit {
  signedFile: ISignedFile | null = null;

  account: Account | null = null;

  state = '';
  position = '';

  @ViewChild('canvas', { static: true }) canvas?: ElementRef<HTMLCanvasElement>;
  ctx!: CanvasRenderingContext2D;

  private pageRendering!: boolean;
  private pdfDoc: any;
  private pdfScale = 1;
  private pageNumPending = null;
  public pageNum = 1;
  public totalPageNum = 0;

  constructor(
    protected dataUtils: JhiDataUtils,
    protected activatedRoute: ActivatedRoute,
    private accountService: AccountService,
    private loginService: LoginService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ signedFile }) => (this.signedFile = signedFile));
    this.accountService.identity().subscribe((account: Account | null) => (this.account = account));
  }

  ngAfterViewInit(): void {
    // eslint-disable-next-line no-console
    console.log('CAnvas: ', this.canvas);
    this.ctx = this.canvas!.nativeElement.getContext('2d') as CanvasRenderingContext2D;
    this.loadPDF();
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.loginService.login();
  }
  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  previousState(): void {
    window.history.back();
  }

  public dragStarted(event: CdkDragStart): any {
    this.state = 'dragStarted';
  }

  public dragEnded(event: CdkDragEnd): any {
    this.state = 'dragEnded';
  }

  public dragMoved(event: CdkDragMove): any {
    this.position = ` > Position X: ${event.pointerPosition.x} - Y: ${event.pointerPosition.y}`;
  }

  loadPDF(): void {
    const pdfData = atob(this.signedFile?.fileBytes);

    const loadingTask = pdfjsLib.getDocument({ data: pdfData });

    loadingTask.promise.then((pdf): void => {
      // eslint-disable-next-line no-console
      console.log('pdf: ', pdf);
      // eslint-disable-next-line no-console
      console.log('PDF loaded');

      this.pdfDoc = pdf;
      this.totalPageNum = this.pdfDoc.numPages;
      // Fetch the first page
      const pageNumber = 1;
      pdf.getPage(pageNumber).then((page): void => {
        const scale = 1;
        const Viewport = page.getViewport({ scale });

        // Prepare canvas using PDF page dimensions
        // var canvas: any = document.getElementById('the-canvas');
        // var context = canvas.getContext('2d');
        this.canvas!.nativeElement.height = Viewport.height;
        this.canvas!.nativeElement.width = Viewport.width;

        // Render PDF page into canvas context
        const renderContext = {
          canvasContext: this.ctx,
          viewport: Viewport,
        };
        const renderTask = page.render(renderContext);
        renderTask.promise.then(() => {});
      });
    });
  }

  renderPage(num: number): void {
    this.pageRendering = true;

    // Using promise to fetch the page
    this.pdfDoc
      .getPage(num)
      .then(
        (page: {
          getViewport: (arg0: { scale: number }) => any;
          render: (arg0: { canvasContext: CanvasRenderingContext2D; viewport: any }) => any;
        }): void => {
          const scale = 1;
          const Viewport = page.getViewport({ scale });

          this.canvas!.nativeElement.height = Viewport.height;
          this.canvas!.nativeElement.width = Viewport.width;

          // Render PDF page into canvas context
          const renderContext = {
            canvasContext: this.ctx,
            viewport: Viewport,
          };
          const renderTask = page.render(renderContext);

          // Wait for rendering to finish
          renderTask.promise.then(() => {
            this.pageRendering = false;
            if (this.pageNumPending !== null) {
              // New page rendering is pending
              this.renderPage(this.pageNumPending!);
              this.pageNumPending = null;
            }
          });
        }
      );
  }

  queueRenderPage(num: any): void {
    if (this.pageRendering) {
      this.pageNumPending = num;
    } else {
      this.renderPage(num);
    }
  }
  onClickPreviousePage(): void {
    if (this.pageNum <= 1) {
      return;
    }
    this.pageNum--;
    this.queueRenderPage(this.pageNum);
  }

  onClickNextPage(): void {
    if (this.pageNum >= this.pdfDoc.numPages) {
      return;
    }
    this.pageNum++;
    this.queueRenderPage(this.pageNum);
  }
}
