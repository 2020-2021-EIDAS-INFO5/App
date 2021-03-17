import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PolySignTestModule } from '../../../test.module';
import { SignaturePlacementComponent } from 'app/entities/signature-placement/signature-placement.component';
import { SignaturePlacementService } from 'app/entities/signature-placement/signature-placement.service';
import { SignaturePlacement } from 'app/shared/model/signature-placement.model';

describe('Component Tests', () => {
  describe('SignaturePlacement Management Component', () => {
    let comp: SignaturePlacementComponent;
    let fixture: ComponentFixture<SignaturePlacementComponent>;
    let service: SignaturePlacementService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PolySignTestModule],
        declarations: [SignaturePlacementComponent],
      })
        .overrideTemplate(SignaturePlacementComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SignaturePlacementComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SignaturePlacementService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new SignaturePlacement(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.signaturePlacements && comp.signaturePlacements[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
