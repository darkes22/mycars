import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IIncidente, Incidente } from 'app/shared/model/incidente.model';
import { IncidenteService } from './incidente.service';
import { IncidenteComponent } from './incidente.component';
import { IncidenteDetailComponent } from './incidente-detail.component';
import { IncidenteUpdateComponent } from './incidente-update.component';

@Injectable({ providedIn: 'root' })
export class IncidenteResolve implements Resolve<IIncidente> {
  constructor(private service: IncidenteService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIncidente> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((incidente: HttpResponse<Incidente>) => {
          if (incidente.body) {
            return of(incidente.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Incidente());
  }
}

export const incidenteRoute: Routes = [
  {
    path: '',
    component: IncidenteComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'mycarsApp.incidente.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: IncidenteDetailComponent,
    resolve: {
      incidente: IncidenteResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'mycarsApp.incidente.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: IncidenteUpdateComponent,
    resolve: {
      incidente: IncidenteResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'mycarsApp.incidente.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: IncidenteUpdateComponent,
    resolve: {
      incidente: IncidenteResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'mycarsApp.incidente.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
