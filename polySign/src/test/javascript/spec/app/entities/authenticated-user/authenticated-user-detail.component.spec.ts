import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PolySignTestModule } from '../../../test.module';
import { AuthenticatedUserDetailComponent } from 'app/entities/authenticated-user/authenticated-user-detail.component';
import { AuthenticatedUser } from 'app/shared/model/authenticated-user.model';

describe('Component Tests', () => {
  describe('AuthenticatedUser Management Detail Component', () => {
    let comp: AuthenticatedUserDetailComponent;
    let fixture: ComponentFixture<AuthenticatedUserDetailComponent>;
    const route = ({ data: of({ authenticatedUser: new AuthenticatedUser(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PolySignTestModule],
        declarations: [AuthenticatedUserDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(AuthenticatedUserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AuthenticatedUserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load authenticatedUser on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.authenticatedUser).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
