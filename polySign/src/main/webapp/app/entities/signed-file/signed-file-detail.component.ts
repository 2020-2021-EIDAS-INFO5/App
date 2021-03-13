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
  styleUrls: ['./rect.scss'],
})
export class SignedFileDetailComponent implements OnInit, AfterViewInit {
  signedFile: ISignedFile | null = null;

  account: Account | null = null;

  state = '';
  position = '';

  @ViewChild('canvas', { static: true }) canvas?: ElementRef<HTMLCanvasElement>;
  ctx!: CanvasRenderingContext2D;

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

    // const loadingTask = pdfjsLib.getDocument(pdfData);

    const pdfUrl = encodeURI('https://raw.githubusercontent.com/mozilla/pdf.js/ba2edeae/examples/learning/helloworld.pdf');

    const loadingTask = pdfjsLib.getDocument(pdfUrl);

    loadingTask.promise.then((pdf): void => {
      // eslint-disable-next-line no-console
      console.log('pdf: ', pdf);
      // eslint-disable-next-line no-console
      console.log('PDF loaded');
      // Fetch the first page
      const pageNumber = 1;
      pdf.getPage(pageNumber).then((page): void => {
        const scale = 1.5;
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
}
