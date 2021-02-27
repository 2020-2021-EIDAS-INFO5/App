import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PolySignTestModule } from '../../../test.module';
import { SignedFileUpdateComponent } from 'app/entities/signed-file/signed-file-update.component';
import { SignedFileService } from 'app/entities/signed-file/signed-file.service';
import { SignedFile } from 'app/shared/model/signed-file.model';

describe('Component Tests', () => {
  describe('SignedFile Management Update Component', () => {
    let comp: SignedFileUpdateComponent;
    let fixture: ComponentFixture<SignedFileUpdateComponent>;
    let service: SignedFileService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PolySignTestModule],
        declarations: [SignedFileUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(SignedFileUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SignedFileUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SignedFileService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new SignedFile(123);
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
        const entity = new SignedFile();
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
