import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PolySignTestModule } from '../../../test.module';
import { SignaturePlacementDetailComponent } from 'app/entities/signature-placement/signature-placement-detail.component';
import { SignaturePlacement } from 'app/shared/model/signature-placement.model';

describe('Component Tests', () => {
  describe('SignaturePlacement Management Detail Component', () => {
    let comp: SignaturePlacementDetailComponent;
    let fixture: ComponentFixture<SignaturePlacementDetailComponent>;
    const route = ({ data: of({ signaturePlacement: new SignaturePlacement(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PolySignTestModule],
        declarations: [SignaturePlacementDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(SignaturePlacementDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SignaturePlacementDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load signaturePlacement on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.signaturePlacement).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
