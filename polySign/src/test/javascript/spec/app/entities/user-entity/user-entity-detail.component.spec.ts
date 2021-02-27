import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PolySignTestModule } from '../../../test.module';
import { UserEntityDetailComponent } from 'app/entities/user-entity/user-entity-detail.component';
import { UserEntity } from 'app/shared/model/user-entity.model';

describe('Component Tests', () => {
  describe('UserEntity Management Detail Component', () => {
    let comp: UserEntityDetailComponent;
    let fixture: ComponentFixture<UserEntityDetailComponent>;
    const route = ({ data: of({ userEntity: new UserEntity(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PolySignTestModule],
        declarations: [UserEntityDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(UserEntityDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UserEntityDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load userEntity on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.userEntity).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
