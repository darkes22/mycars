import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IIncidente } from 'app/shared/model/incidente.model';

type EntityResponseType = HttpResponse<IIncidente>;
type EntityArrayResponseType = HttpResponse<IIncidente[]>;

@Injectable({ providedIn: 'root' })
export class IncidenteService {
  public resourceUrl = SERVER_API_URL + 'api/incidentes';

  constructor(protected http: HttpClient) {}

  create(incidente: IIncidente): Observable<EntityResponseType> {
    return this.http.post<IIncidente>(this.resourceUrl, incidente, { observe: 'response' });
  }

  update(incidente: IIncidente): Observable<EntityResponseType> {
    return this.http.put<IIncidente>(this.resourceUrl, incidente, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIncidente>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIncidente[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
