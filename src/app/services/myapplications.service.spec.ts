import { TestBed } from '@angular/core/testing';
import { MyApplicationsService } from './myapplications.service';

describe('MyapplicationsService', () => {
  let service: MyApplicationsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MyApplicationsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
