import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPropietario } from 'app/shared/model/propietario.model';

@Component({
  selector: 'jhi-propietario-detail',
  templateUrl: './propietario-detail.component.html'
})
export class PropietarioDetailComponent implements OnInit {
  propietario: IPropietario | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ propietario }) => {
      this.propietario = propietario;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
