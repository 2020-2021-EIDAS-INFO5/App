import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PolySignTestModule } from '../../../test.module';
import { SignatureProcessUpdateComponent } from 'app/entities/signature-process/signature-process-update.component';
import { SignatureProcessService } from 'app/entities/signature-process/signature-process.service';
import { SignatureProcess } from 'app/shared/model/signature-process.model';

describe('Component Tests', () => {
  describe('SignatureProcess Management Update Component', () => {
    let comp: SignatureProcessUpdateComponent;
    let fixture: ComponentFixture<SignatureProcessUpdateComponent>;
    let service: SignatureProcessService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PolySignTestModule],
        declarations: [SignatureProcessUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(SignatureProcessUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SignatureProcessUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SignatureProcessService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new SignatureProcess(123);
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
        const entity = new SignatureProcess();
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
