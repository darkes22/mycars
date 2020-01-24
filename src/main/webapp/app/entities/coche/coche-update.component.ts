import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';

import { ICoche, Coche } from 'app/shared/model/coche.model';
import { CocheService } from './coche.service';

@Component({
  selector: 'jhi-coche-update',
  templateUrl: './coche-update.component.html'
})
export class CocheUpdateComponent implements OnInit {
  isSaving = false;
  fechaventaDp: any;

  editForm = this.fb.group({
    id: [],
    marca: [],
    modelo: [],
    matricula: [null, []],
    precio: [],
    vendido: [],
    fechaventa: [],
    owner: []
  });

  constructor(protected cocheService: CocheService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ coche }) => {
      this.updateForm(coche);
    });
  }

  updateForm(coche: ICoche): void {
    this.editForm.patchValue({
      id: coche.id,
      marca: coche.marca,
      modelo: coche.modelo,
      matricula: coche.matricula,
      precio: coche.precio,
      vendido: coche.vendido,
      fechaventa: coche.fechaventa,
      owner: coche.owner
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const coche = this.createFromForm();
    if (!coche.vendido) {
      coche.owner = undefined;
      coche.fechaventa = undefined;
    }
    if (coche.id !== undefined) {
      this.subscribeToSaveResponse(this.cocheService.update(coche));
    } else {
      this.subscribeToSaveResponse(this.cocheService.create(coche));
    }
  }

  private createFromForm(): ICoche {
    return {
      ...new Coche(),
      id: this.editForm.get(['id'])!.value,
      marca: this.editForm.get(['marca'])!.value,
      modelo: this.editForm.get(['modelo'])!.value,
      matricula: this.editForm.get(['matricula'])!.value,
      precio: this.editForm.get(['precio'])!.value,
      vendido: this.editForm.get(['vendido'])!.value,
      fechaventa: this.editForm.get(['fechaventa'])!.value,
      owner: this.editForm.get(['owner'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICoche>>): void {
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
}
