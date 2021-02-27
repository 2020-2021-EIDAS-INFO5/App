import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PolySignTestModule } from '../../../test.module';
import { AuthenticatedUserUpdateComponent } from 'app/entities/authenticated-user/authenticated-user-update.component';
import { AuthenticatedUserService } from 'app/entities/authenticated-user/authenticated-user.service';
import { AuthenticatedUser } from 'app/shared/model/authenticated-user.model';

describe('Component Tests', () => {
  describe('AuthenticatedUser Management Update Component', () => {
    let comp: AuthenticatedUserUpdateComponent;
    let fixture: ComponentFixture<AuthenticatedUserUpdateComponent>;
    let service: AuthenticatedUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PolySignTestModule],
        declarations: [AuthenticatedUserUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(AuthenticatedUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AuthenticatedUserUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AuthenticatedUserService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new AuthenticatedUser(123);
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
        const entity = new AuthenticatedUser();
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
