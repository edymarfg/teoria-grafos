import { TestBed } from '@angular/core/testing';

import { FormGrafosService } from './form-grafos.service';

describe('FormGrafosService', () => {
  let service: FormGrafosService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FormGrafosService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
