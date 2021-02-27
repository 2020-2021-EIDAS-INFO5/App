import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PolySignTestModule } from '../../../test.module';
import { AuthDetailComponent } from 'app/entities/auth/auth-detail.component';
import { Auth } from 'app/shared/model/auth.model';

describe('Component Tests', () => {
  describe('Auth Management Detail Component', () => {
    let comp: AuthDetailComponent;
    let fixture: ComponentFixture<AuthDetailComponent>;
    const route = ({ data: of({ auth: new Auth(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PolySignTestModule],
        declarations: [AuthDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(AuthDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AuthDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load auth on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.auth).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
