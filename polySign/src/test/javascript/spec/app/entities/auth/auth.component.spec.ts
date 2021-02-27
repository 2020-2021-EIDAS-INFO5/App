import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PolySignTestModule } from '../../../test.module';
import { AuthComponent } from 'app/entities/auth/auth.component';
import { AuthService } from 'app/entities/auth/auth.service';
import { Auth } from 'app/shared/model/auth.model';

describe('Component Tests', () => {
  describe('Auth Management Component', () => {
    let comp: AuthComponent;
    let fixture: ComponentFixture<AuthComponent>;
    let service: AuthService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PolySignTestModule],
        declarations: [AuthComponent],
      })
        .overrideTemplate(AuthComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AuthComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AuthService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Auth(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.auths && comp.auths[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
