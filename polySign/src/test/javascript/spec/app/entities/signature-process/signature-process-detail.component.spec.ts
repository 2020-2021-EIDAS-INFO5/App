import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PolySignTestModule } from '../../../test.module';
import { SignatureProcessDetailComponent } from 'app/entities/signature-process/signature-process-detail.component';
import { SignatureProcess } from 'app/shared/model/signature-process.model';

describe('Component Tests', () => {
  describe('SignatureProcess Management Detail Component', () => {
    let comp: SignatureProcessDetailComponent;
    let fixture: ComponentFixture<SignatureProcessDetailComponent>;
    const route = ({ data: of({ signatureProcess: new SignatureProcess(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PolySignTestModule],
        declarations: [SignatureProcessDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(SignatureProcessDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SignatureProcessDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load signatureProcess on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.signatureProcess).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
