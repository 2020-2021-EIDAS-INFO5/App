import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PolySignTestModule } from '../../../test.module';
import { AuthoritDetailComponent } from 'app/entities/authorit/authorit-detail.component';
import { Authorit } from 'app/shared/model/authorit.model';

describe('Component Tests', () => {
  describe('Authorit Management Detail Component', () => {
    let comp: AuthoritDetailComponent;
    let fixture: ComponentFixture<AuthoritDetailComponent>;
    const route = ({ data: of({ authorit: new Authorit(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PolySignTestModule],
        declarations: [AuthoritDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(AuthoritDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AuthoritDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load authorit on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.authorit).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
