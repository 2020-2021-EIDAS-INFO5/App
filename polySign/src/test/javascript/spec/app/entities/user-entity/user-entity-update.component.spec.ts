import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PolySignTestModule } from '../../../test.module';
import { UserEntityUpdateComponent } from 'app/entities/user-entity/user-entity-update.component';
import { UserEntityService } from 'app/entities/user-entity/user-entity.service';
import { UserEntity } from 'app/shared/model/user-entity.model';

describe('Component Tests', () => {
  describe('UserEntity Management Update Component', () => {
    let comp: UserEntityUpdateComponent;
    let fixture: ComponentFixture<UserEntityUpdateComponent>;
    let service: UserEntityService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PolySignTestModule],
        declarations: [UserEntityUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(UserEntityUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UserEntityUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserEntityService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new UserEntity(123);
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
        const entity = new UserEntity();
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
