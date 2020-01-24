import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IIncidente, Incidente } from 'app/shared/model/incidente.model';
import { IncidenteService } from './incidente.service';
import { ICoche } from 'app/shared/model/coche.model';
import { CocheService } from 'app/entities/coche/coche.service';

@Component({
  selector: 'jhi-incidente-update',
  templateUrl: './incidente-update.component.html'
})
export class IncidenteUpdateComponent implements OnInit {
  isSaving = false;

  coches: ICoche[] = [];

  editForm = this.fb.group({
    id: [],
    fallo: [],
    descripcion: [],
    coche: []
  });

  constructor(
    protected incidenteService: IncidenteService,
    protected cocheService: CocheService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ incidente }) => {
      this.updateForm(incidente);

      this.cocheService
        .query()
        .pipe(
          map((res: HttpResponse<ICoche[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ICoche[]) => (this.coches = resBody));
    });
  }

  updateForm(incidente: IIncidente): void {
    this.editForm.patchValue({
      id: incidente.id,
      fallo: incidente.fallo,
      descripcion: incidente.descripcion,
      coche: incidente.coche
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const incidente = this.createFromForm();
    if (incidente.id !== undefined) {
      this.subscribeToSaveResponse(this.incidenteService.update(incidente));
    } else {
      this.subscribeToSaveResponse(this.incidenteService.create(incidente));
    }
  }

  private createFromForm(): IIncidente {
    return {
      ...new Incidente(),
      id: this.editForm.get(['id'])!.value,
      fallo: this.editForm.get(['fallo'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      coche: this.editForm.get(['coche'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIncidente>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: ICoche): any {
    return item.id;
  }
}
