import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MycarsTestModule } from '../../../test.module';
import { PropietarioDetailComponent } from 'app/entities/propietario/propietario-detail.component';
import { Propietario } from 'app/shared/model/propietario.model';

describe('Component Tests', () => {
  describe('Propietario Management Detail Component', () => {
    let comp: PropietarioDetailComponent;
    let fixture: ComponentFixture<PropietarioDetailComponent>;
    const route = ({ data: of({ propietario: new Propietario(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MycarsTestModule],
        declarations: [PropietarioDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PropietarioDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PropietarioDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load propietario on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.propietario).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
