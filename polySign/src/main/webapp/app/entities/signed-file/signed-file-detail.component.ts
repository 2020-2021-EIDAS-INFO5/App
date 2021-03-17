import { Component, OnInit, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';
import { LoginService } from 'app/core/login/login.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { ISignedFile } from 'app/shared/model/signed-file.model';
// import { CdkDragEnd, CdkDragStart, CdkDragMove } from '@angular/cdk/drag-drop';
import * as pdfjsLib from 'pdfjs-dist';
import interact from 'interactjs';

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

  guessX = 0;
  guessY = 0;
  mouse = '';
  maxPDFx = 595;
  maxPDFy = 842;
  offsetY = 7;

  @ViewChild('canvas', { static: true }) canvas?: ElementRef<HTMLCanvasElement>;
  ctx!: CanvasRenderingContext2D;

  @ViewChild('sign', { static: true }) sign?: ElementRef<HTMLCanvasElement>;

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

    const maxPDFx = 595;
    const maxPDFy = 842;
    const offsetY = 7;

    // enable draggables to be dropped into this
    interact('.dropzone').dropzone({
      // only accept elements matching this CSS selector
      accept: '.drag-drop',
      // Require a 100% element overlap for a drop to be possible
      overlap: 1,

      // listen for drop related events:

      ondropactivate(event): void {
        // add active dropzone feedback
        event.target.classList.add('drop-active');
      },
      ondragenter(event: { relatedTarget: any; target: any }): void {
        const draggableElement = event.relatedTarget,
          dropzoneElement = event.target;

        // feedback the possibility of a drop
        dropzoneElement.classList.add('drop-target');
        draggableElement.classList.add('can-drop');
        draggableElement.classList.remove('dropped-out');
        // draggableElement.textContent = 'Dragged in';
      },
      ondragleave(event): void {
        // remove the drop feedback style
        event.target.classList.remove('drop-target');
        event.relatedTarget.classList.remove('can-drop');
        event.relatedTarget.classList.add('dropped-out');
        // event.relatedTarget.textContent = 'Dragged out';
      },
      ondrop(event: any): void {
        // event.relatedTarget.textContent = 'Dropped';
      },
      ondropdeactivate(event): void {
        // remove active dropzone feedback
        event.target.classList.remove('drop-active');
        event.target.classList.remove('drop-target');
      },
    });
    function dragMoveListener(event: any): void {
      const target = event.target;
      /* keep the dragged position in the data-x/data-y attributes */
      const x = (parseFloat(target.getAttribute('data-x')) || 0) + event.dx;
      const y = (parseFloat(target.getAttribute('data-y')) || 0) + event.dy;

      /* translate the element */
      target.style.webkitTransform = target.style.transform = 'translate(' + x + 'px, ' + y + 'px)';

      /* update the posiion attributes */
      target.setAttribute('data-x', x);
      target.setAttribute('data-y', y);
    }

    interact('.drag-drop').draggable({
      inertia: true,
      modifiers: [
        interact.modifiers.restrictRect({
          restriction: 'parent',
          endOnly: true,
        }),
      ],
      autoScroll: true,
      //  dragMoveListener from the dragging demo above
      listeners: { move: dragMoveListener },
    });

    function renderizzaPlaceholder(currentPage: any, parametri: any): void {
      const maxHTMLx = $('#canvas').width();
      const maxHTMLy = $('#canvas').height();

      const paramContainerWidth = $('#parametriContainer').width();
      let yCounterOfGenerated = 0;
      const numOfMaxItem = 25;
      const notValidHeight = 30;
      let y = 0;
      const x = 6;

      const totalPages = Math.ceil(parametri.length / numOfMaxItem);

      for (let i = 0; i < parametri.length; i++) {
        const param = parametri[i];
        const page = Math.floor(i / numOfMaxItem);
        const display = currentPage === page ? 'block' : 'none';

        if (i > 0 && i % numOfMaxItem === 0) {
          yCounterOfGenerated = 0;
        }

        let classStyle = '';
        let valore = param.valore;
        /*il placeholder non è valido: lo incolonna a sinistra*/

        if (i > 0 && i % numOfMaxItem === 0) {
          yCounterOfGenerated = 0;
        }

        classStyle = '';
        valore = param.valore;
        /* il placeholder non è valido: lo incolonna a sinistra*/
        y = yCounterOfGenerated;
        yCounterOfGenerated += notValidHeight;
        classStyle = 'drag-drop dropped-out';

        $('#parametriContainer').append(
          '<div class="' +
            classStyle +
            '" data-id="-1" data-page="' +
            page +
            '" data-toggle="' +
            valore +
            '" data-valore="' +
            valore +
            '" data-x="' +
            x +
            '" data-y="' +
            y +
            '" style="transform: translate(' +
            x +
            'px, ' +
            y +
            'px); display:' +
            display +
            '">  <span class="circle"></span><span class="descrizione">' +
            param.descrizione +
            ' </span></div>'
        );
      }

      y = notValidHeight * (numOfMaxItem + 1);
      let prevStyle = '';
      let nextStyle = '';
      let prevDisabled = false;
      let nextDisabled = false;
      if (currentPage === 0) {
        prevStyle = 'disabled';
        prevDisabled = true;
      }

      if (currentPage >= totalPages - 1 || totalPages === 1) {
        nextDisabled = true;
        nextStyle = 'disabled';
      }

      // Aggiunge la paginazione
      $('#parametriContainer').append(
        '<ul id="pager" class="pager" style="transform: translate(' +
          x +
          'px, ' +
          y +
          'px); width:200px;"><li onclick="changePage(' +
          prevDisabled +
          ',' +
          currentPage +
          ',-1)" class="page-item ' +
          prevStyle +
          '"><span>«</span></li><li onclick="changePage(' +
          nextDisabled +
          ',' +
          currentPage +
          ',1)" class="page-item ' +
          nextStyle +
          '" style="margin-left:10px;"><span>&raquo;</span></li></ul>'
      );
    }
    function renderizzaInPagina(this: any, parametri: any): void {
      const maxHTMLx = $('#canvas').width();
      const maxHTMLy = $('#canvas').height();

      const paramContainerWidth = $('#parametriContainer').width();
      const yCounterOfGenerated = 0;
      const numOfMaxItem = 26;
      const notValidHeight = 30;
      let y = 0;
      let x = 6;
      for (let i = 0; i < parametri.length; i++) {
        const param = parametri[i];

        const classStyle = 'drag-drop can-drop';
        const valore = param.valore;
        /* il placeholder non è valido: lo incolonna a sinistra*/

        const pdfY = this.maxPDFy - param.posizioneY - this.offsetY;
        y = (pdfY * maxHTMLy!) / this.maxPDFy;
        x = (param.posizioneX * maxHTMLx!) / this.maxPDFx + paramContainerWidth!;

        $('#parametriContainer').append(
          '<div class="' +
            classStyle +
            '" data-id="' +
            param.idParametroModulo +
            '" data-toggle="' +
            valore +
            '" data-valore="' +
            valore +
            '" data-x="' +
            x +
            '" data-y="' +
            y +
            '" style="transform: translate(' +
            x +
            'px, ' +
            y +
            'px);">  <span class="circle"></span><span class="descrizione">' +
            param.descrizione +
            ' </span></div>'
        );
      }
    }

    function changePage(disabled: boolean, currentPage: number, delta: any): void {
      if (disabled) {
        return;
      }

      /*recupera solo i parametri non posizionati in pagina*/
      const parametri: { valore: any; descrizione: string; posizioneX: number; posizioneY: number }[] = [];
      $('.drag-drop.dropped-out').each(function (): void {
        const valore = $(this).data('valore');
        const descrizione = $(this).find('.descrizione').text();
        parametri.push({ valore, descrizione, posizioneX: -1000, posizioneY: -1000 });
        $(this).remove();
      });

      // svuota il contentitore
      $('#pager').remove();
      currentPage += delta;
      renderizzaPlaceholder(currentPage, parametri);
    }

    $(document).bind('pagerendered', function (e): void {
      $('#pdfManager').show();
      const param = JSON.parse($('#parameters').val() as string);
      $('#parametriContainer').empty();
      renderizzaPlaceholder(0, param);
    });
  }

  public showCoordinates(): void {
    const validi: { descrizione: string; posizioneX: number; posizioneY: number; valore: any }[] = [];
    const nonValidi = [];
    const maxPDFx = 595;
    const maxPDFy = 842;
    const offsetY = 7;

    const maxHTMLx = $('#canvas').width();
    const maxHTMLy = $('#canvas').height();
    const paramContainerWidth = $('#parametriContainer').width();

    // recupera tutti i placholder validi
    $('.drag-drop.can-drop').each(function (index): void {
      const x = parseFloat($(this).data('x'));
      const y = parseFloat($(this).data('y'));
      const valore = $(this).data('valore');
      const descrizione = $(this).find('.descrizione').text();

      const pdfY = (y * maxPDFy) / maxHTMLy!;
      const posizioneY = maxPDFy - offsetY - pdfY;
      const posizioneX = (x * maxPDFx) / maxHTMLx! - paramContainerWidth!;

      const val = { descrizione, posizioneX, posizioneY, valore };
      validi.push(val);
    });

    if (validi.length === 0) {
      alert('No placeholder dragged into document');
    } else {
      alert(JSON.stringify(validi));
    }
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

  /*  public dragStarted(event: CdkDragStart): any {
    this.state = 'dragStarted';
  }

  public dragEnded(event: CdkDragEnd): any {
    this.state = 'dragEnded';
  }

  public dragMoved(event: CdkDragMove): any {
    this.position = ` > Position X: ${event.pointerPosition.x} - Y: ${event.pointerPosition.y}`;
  } */

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

  public storeGuess(event: { offsetX: any; offsetY: any }): any {
    const x = event.offsetX;
    const y = event.offsetY;
    this.guessX = x;
    this.guessY = y;
    this.mouse = `x coords: " + ${this.guessX}+ ", y coords: " + ${this.guessY})`;
    this.ctx.strokeStyle = 'green';
    this.ctx.strokeRect(this.guessX, this.guessY, 100, 50);
    this.ctx.save();
  }
}
