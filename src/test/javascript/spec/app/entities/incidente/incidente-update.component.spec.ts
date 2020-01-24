import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { MycarsTestModule } from '../../../test.module';
import { IncidenteUpdateComponent } from 'app/entities/incidente/incidente-update.component';
import { IncidenteService } from 'app/entities/incidente/incidente.service';
import { Incidente } from 'app/shared/model/incidente.model';

describe('Component Tests', () => {
  describe('Incidente Management Update Component', () => {
    let comp: IncidenteUpdateComponent;
    let fixture: ComponentFixture<IncidenteUpdateComponent>;
    let service: IncidenteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MycarsTestModule],
        declarations: [IncidenteUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(IncidenteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IncidenteUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(IncidenteService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Incidente(123);
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
        const entity = new Incidente();
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
