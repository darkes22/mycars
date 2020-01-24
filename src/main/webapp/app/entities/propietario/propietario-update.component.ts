import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPropietario, Propietario } from 'app/shared/model/propietario.model';
import { PropietarioService } from './propietario.service';

@Component({
  selector: 'jhi-propietario-update',
  templateUrl: './propietario-update.component.html'
})
export class PropietarioUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [],
    apellidos: [],
    dni: []
  });

  constructor(protected propietarioService: PropietarioService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ propietario }) => {
      this.updateForm(propietario);
    });
  }

  updateForm(propietario: IPropietario): void {
    this.editForm.patchValue({
      id: propietario.id,
      nombre: propietario.nombre,
      apellidos: propietario.apellidos,
      dni: propietario.dni
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const propietario = this.createFromForm();
    if (propietario.id !== undefined) {
      this.subscribeToSaveResponse(this.propietarioService.update(propietario));
    } else {
      this.subscribeToSaveResponse(this.propietarioService.create(propietario));
    }
  }

  private createFromForm(): IPropietario {
    return {
      ...new Propietario(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      apellidos: this.editForm.get(['apellidos'])!.value,
      dni: this.editForm.get(['dni'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPropietario>>): void {
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
