import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PolySignTestModule } from '../../../test.module';
import { SignOrderDetailComponent } from 'app/entities/sign-order/sign-order-detail.component';
import { SignOrder } from 'app/shared/model/sign-order.model';

describe('Component Tests', () => {
  describe('SignOrder Management Detail Component', () => {
    let comp: SignOrderDetailComponent;
    let fixture: ComponentFixture<SignOrderDetailComponent>;
    const route = ({ data: of({ signOrder: new SignOrder(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PolySignTestModule],
        declarations: [SignOrderDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(SignOrderDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SignOrderDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load signOrder on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.signOrder).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
