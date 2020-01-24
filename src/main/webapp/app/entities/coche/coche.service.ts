import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICoche } from 'app/shared/model/coche.model';

type EntityResponseType = HttpResponse<ICoche>;
type EntityArrayResponseType = HttpResponse<ICoche[]>;

@Injectable({ providedIn: 'root' })
export class CocheService {
  public resourceUrl = SERVER_API_URL + 'api/coches';

  constructor(protected http: HttpClient) {}

  create(coche: ICoche): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(coche);
    return this.http
      .post<ICoche>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(coche: ICoche): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(coche);
    return this.http
      .put<ICoche>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICoche>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICoche[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(coche: ICoche): ICoche {
    const copy: ICoche = Object.assign({}, coche, {
      fechaventa: coche.fechaventa && coche.fechaventa.isValid() ? coche.fechaventa.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaventa = res.body.fechaventa ? moment(res.body.fechaventa) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((coche: ICoche) => {
        coche.fechaventa = coche.fechaventa ? moment(coche.fechaventa) : undefined;
      });
    }
    return res;
  }
}
