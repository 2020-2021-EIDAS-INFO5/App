import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PolySignTestModule } from '../../../test.module';
import { SignOrderUpdateComponent } from 'app/entities/sign-order/sign-order-update.component';
import { SignOrderService } from 'app/entities/sign-order/sign-order.service';
import { SignOrder } from 'app/shared/model/sign-order.model';

describe('Component Tests', () => {
  describe('SignOrder Management Update Component', () => {
    let comp: SignOrderUpdateComponent;
    let fixture: ComponentFixture<SignOrderUpdateComponent>;
    let service: SignOrderService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PolySignTestModule],
        declarations: [SignOrderUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(SignOrderUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SignOrderUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SignOrderService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new SignOrder(123);
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
        const entity = new SignOrder();
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
