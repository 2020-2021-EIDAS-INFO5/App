import { Component, OnInit, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';
import { LoginService } from 'app/core/login/login.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { ISignedFile } from 'app/shared/model/signed-file.model';
import { CdkDragEnd, CdkDragStart, CdkDragMove } from '@angular/cdk/drag-drop';

/*const HummusRecipe = require('hummus-recipe');

const pdfEditor = async () => {
  const pdfDoc = new HummusRecipe("./pdf/demo.pdf", "./pdf/output.pdf");
  pdfDoc
    // edit 1st page
    .editPage(1)
    .text("Add some texts to an existing pdf file", 150, 500, {
      color: "003240"
    })
    .image("../../../content/images/signature.png", 100, 600, {
      width: 100,
      keepAspectRatio: true
    })
    .endPage()
    .endPDF();
  }*/

@Component({
  selector: 'jhi-signed-file-detail',
  templateUrl: './signed-file-detail.component.html',
  styleUrls: ['./rect.scss'],
})
export class SignedFileDetailComponent implements OnInit {
  signedFile: ISignedFile | null = null;

  account: Account | null = null;

  state = '';
  position = '';

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
}
