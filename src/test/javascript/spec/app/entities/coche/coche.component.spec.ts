import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MycarsTestModule } from '../../../test.module';
import { CocheComponent } from 'app/entities/coche/coche.component';
import { CocheService } from 'app/entities/coche/coche.service';
import { Coche } from 'app/shared/model/coche.model';

describe('Component Tests', () => {
  describe('Coche Management Component', () => {
    let comp: CocheComponent;
    let fixture: ComponentFixture<CocheComponent>;
    let service: CocheService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MycarsTestModule],
        declarations: [CocheComponent],
        providers: []
      })
        .overrideTemplate(CocheComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CocheComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CocheService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Coche(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.coches && comp.coches[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
