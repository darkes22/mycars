import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MycarsTestModule } from '../../../test.module';
import { PropietarioComponent } from 'app/entities/propietario/propietario.component';
import { PropietarioService } from 'app/entities/propietario/propietario.service';
import { Propietario } from 'app/shared/model/propietario.model';

describe('Component Tests', () => {
  describe('Propietario Management Component', () => {
    let comp: PropietarioComponent;
    let fixture: ComponentFixture<PropietarioComponent>;
    let service: PropietarioService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MycarsTestModule],
        declarations: [PropietarioComponent],
        providers: []
      })
        .overrideTemplate(PropietarioComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PropietarioComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PropietarioService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Propietario(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.propietarios && comp.propietarios[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
