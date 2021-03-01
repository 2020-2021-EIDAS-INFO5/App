import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { SignatureProcessService } from 'app/entities/signature-process/signature-process.service';
import { ISignatureProcess, SignatureProcess } from 'app/shared/model/signature-process.model';
import { Status } from 'app/shared/model/enumerations/status.model';

describe('Service Tests', () => {
  describe('SignatureProcess Service', () => {
    let injector: TestBed;
    let service: SignatureProcessService;
    let httpMock: HttpTestingController;
    let elemDefault: ISignatureProcess;
    let expectedResult: ISignatureProcess | ISignatureProcess[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(SignatureProcessService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new SignatureProcess(0, 'AAAAAAA', currentDate, currentDate, Status.COMPLETED, false);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            emissionDate: currentDate.format(DATE_TIME_FORMAT),
            expirationDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a SignatureProcess', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            emissionDate: currentDate.format(DATE_TIME_FORMAT),
            expirationDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            emissionDate: currentDate,
            expirationDate: currentDate,
          },
          returnedFromService
        );

        service.create(new SignatureProcess()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SignatureProcess', () => {
        const returnedFromService = Object.assign(
          {
            title: 'BBBBBB',
            emissionDate: currentDate.format(DATE_TIME_FORMAT),
            expirationDate: currentDate.format(DATE_TIME_FORMAT),
            status: 'BBBBBB',
            orderedSigning: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            emissionDate: currentDate,
            expirationDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of SignatureProcess', () => {
        const returnedFromService = Object.assign(
          {
            title: 'BBBBBB',
            emissionDate: currentDate.format(DATE_TIME_FORMAT),
            expirationDate: currentDate.format(DATE_TIME_FORMAT),
            status: 'BBBBBB',
            orderedSigning: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            emissionDate: currentDate,
            expirationDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a SignatureProcess', () => {
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
