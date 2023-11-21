import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { OptionLocation } from './../model/OptionLocation';
import { GrafoMatrixLocations } from '../model/GrafoMatrixLocations';
import { GrafoMatrixInput } from '../model/GrafoMatrixInput';
import { Step } from '../model/Step';

@Injectable({
  providedIn: 'root',
})
export class FormGrafosService {
  private readonly API = 'api/grafos';

  constructor(private httpClient: HttpClient) {}

  rotas(input: GrafoMatrixInput): Observable<Step[]> {
    return this.httpClient.post<Step[]>(this.API + '/rotas', input);
  }

  autocomplete(name: string): Observable<OptionLocation[]> {
    return this.httpClient.post<OptionLocation[]>(
      this.API + '/autocomplete',
      name
    );
  }

  matrix(input: GrafoMatrixInput): Observable<GrafoMatrixLocations[]> {
    return this.httpClient.post<GrafoMatrixLocations[]>(
      this.API + '/matrix',
      input
    );
  }
}
