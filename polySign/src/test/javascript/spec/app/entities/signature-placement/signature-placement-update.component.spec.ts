import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PolySignTestModule } from '../../../test.module';
import { SignaturePlacementUpdateComponent } from 'app/entities/signature-placement/signature-placement-update.component';
import { SignaturePlacementService } from 'app/entities/signature-placement/signature-placement.service';
import { SignaturePlacement } from 'app/shared/model/signature-placement.model';

describe('Component Tests', () => {
  describe('SignaturePlacement Management Update Component', () => {
    let comp: SignaturePlacementUpdateComponent;
    let fixture: ComponentFixture<SignaturePlacementUpdateComponent>;
    let service: SignaturePlacementService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PolySignTestModule],
        declarations: [SignaturePlacementUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(SignaturePlacementUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SignaturePlacementUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SignaturePlacementService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new SignaturePlacement(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new SignaturePlacement();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
