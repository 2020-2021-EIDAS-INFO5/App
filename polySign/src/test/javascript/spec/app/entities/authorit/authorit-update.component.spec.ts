import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PolySignTestModule } from '../../../test.module';
import { AuthoritUpdateComponent } from 'app/entities/authorit/authorit-update.component';
import { AuthoritService } from 'app/entities/authorit/authorit.service';
import { Authorit } from 'app/shared/model/authorit.model';

describe('Component Tests', () => {
  describe('Authorit Management Update Component', () => {
    let comp: AuthoritUpdateComponent;
    let fixture: ComponentFixture<AuthoritUpdateComponent>;
    let service: AuthoritService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PolySignTestModule],
        declarations: [AuthoritUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(AuthoritUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AuthoritUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AuthoritService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Authorit(123);
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
        const entity = new Authorit();
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
