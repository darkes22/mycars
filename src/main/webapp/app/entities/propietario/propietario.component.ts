import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPropietario } from 'app/shared/model/propietario.model';
import { PropietarioService } from './propietario.service';
import { PropietarioDeleteDialogComponent } from './propietario-delete-dialog.component';

@Component({
  selector: 'jhi-propietario',
  templateUrl: './propietario.component.html'
})
export class PropietarioComponent implements OnInit, OnDestroy {
  propietarios?: IPropietario[];
  eventSubscriber?: Subscription;

  constructor(
    protected propietarioService: PropietarioService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.propietarioService.query().subscribe((res: HttpResponse<IPropietario[]>) => {
      this.propietarios = res.body ? res.body : [];
    });
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPropietarios();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPropietario): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPropietarios(): void {
    this.eventSubscriber = this.eventManager.subscribe('propietarioListModification', () => this.loadAll());
  }

  delete(propietario: IPropietario): void {
    const modalRef = this.modalService.open(PropietarioDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.propietario = propietario;
  }
}
