import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs';

import { ICoche } from 'app/shared/model/coche.model';
import { CocheService } from './coche.service';
import { CocheDeleteDialogComponent } from './coche-delete-dialog.component';
import { cocheRoute } from './coche.route';

@Component({
  selector: 'jhi-coche',
  templateUrl: './coche.component.html'
})
export class CocheComponent implements OnInit, OnDestroy {
  coches?: ICoche[];
  eventSubscriber?: Subscription;

  estado: String = '';
  jugadamia: String = '';
  jugadadepc: String = '';
  piedraAssets: String = '../../../content/images/piedra.jpg';
  papelAssets: String = '../../../content/images/papel.jpg';
  tijeraAssets: String = '../../../content/images/tijera.jpg';

  constructor(protected cocheService: CocheService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.cocheService.query().subscribe((res: HttpResponse<ICoche[]>) => {
      this.coches = res.body ? res.body : [];
    });
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCoches();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICoche): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCoches(): void {
    this.eventSubscriber = this.eventManager.subscribe('cocheListModification', () => this.loadAll());
  }

  delete(coche: ICoche): void {
    const modalRef = this.modalService.open(CocheDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.coche = coche;
  }

  pulsar(entrada: number): void {
    const jugadapc = Math.floor(Math.random() * 3);

    if (entrada === jugadapc) {
      this.estado = 'Empate';
    }

    if (entrada === 0 && jugadapc === 1) {
      this.estado = 'Ganaste';
      this.jugadamia = 'Papel';
      this.jugadadepc = 'Piedra';
    }

    if (entrada === 0 && jugadapc === 2) {
      this.estado = 'Perdiste';
      this.jugadamia = 'Papel';
      this.jugadadepc = 'Tijera';
    }

    if (entrada === 1 && jugadapc === 0) {
      this.estado = 'Perdiste';
      this.jugadamia = 'Piedra';
      this.jugadadepc = 'Papel';
    }

    if (entrada === 1 && jugadapc === 2) {
      this.estado = 'Ganaste';
      this.jugadamia = 'Piedra';
      this.jugadadepc = 'Tijera';
    }

    if (entrada === 2 && jugadapc === 0) {
      this.estado = 'Ganaste';
      this.jugadamia = 'Tijera';
      this.jugadadepc = 'Papel';
    }

    if (entrada === 2 && jugadapc === 1) {
      this.estado = 'Perdiste';
      this.jugadamia = 'Tijera';
      this.jugadadepc = 'Piedra';
    }
  }

  cambiarEstadoVenta(entrada: ICoche): void {
    entrada.vendido = true;

    const cocheaguardar = entrada;

    if (cocheaguardar.id !== undefined) {
      this.subscribeToSaveResponse(this.cocheService.update(cocheaguardar));
    } else {
      this.subscribeToSaveResponse(this.cocheService.create(cocheaguardar));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICoche>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    alert('Guardado.');
  }

  protected onSaveError(): void {
    alert('No ha sido posible guardar.');
  }
}
