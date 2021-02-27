import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { PolySignTestModule } from '../../../test.module';
import { SignedFileDetailComponent } from 'app/entities/signed-file/signed-file-detail.component';
import { SignedFile } from 'app/shared/model/signed-file.model';

describe('Component Tests', () => {
  describe('SignedFile Management Detail Component', () => {
    let comp: SignedFileDetailComponent;
    let fixture: ComponentFixture<SignedFileDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ signedFile: new SignedFile(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PolySignTestModule],
        declarations: [SignedFileDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(SignedFileDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SignedFileDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load signedFile on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.signedFile).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
