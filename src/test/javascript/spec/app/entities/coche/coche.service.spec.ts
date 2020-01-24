import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { CocheService } from 'app/entities/coche/coche.service';
import { ICoche, Coche } from 'app/shared/model/coche.model';

describe('Service Tests', () => {
  describe('Coche Service', () => {
    let injector: TestBed;
    let service: CocheService;
    let httpMock: HttpTestingController;
    let elemDefault: ICoche;
    let expectedResult: ICoche | ICoche[] | boolean | null;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(CocheService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Coche(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 0, false, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            fechaventa: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Coche', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            fechaventa: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            fechaventa: currentDate
          },
          returnedFromService
        );
        service
          .create(new Coche())
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Coche', () => {
        const returnedFromService = Object.assign(
          {
            marca: 'BBBBBB',
            modelo: 'BBBBBB',
            matricula: 'BBBBBB',
            precio: 1,
            vendido: true,
            fechaventa: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaventa: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Coche', () => {
        const returnedFromService = Object.assign(
          {
            marca: 'BBBBBB',
            modelo: 'BBBBBB',
            matricula: 'BBBBBB',
            precio: 1,
            vendido: true,
            fechaventa: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            fechaventa: currentDate
          },
          returnedFromService
        );
        service
          .query()
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Coche', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
