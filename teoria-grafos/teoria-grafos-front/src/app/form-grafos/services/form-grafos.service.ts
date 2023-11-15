import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OptionLocation } from '../model/OptionLocation';

@Injectable({
  providedIn: 'root',
})
export class FormGrafosService {
  private readonly API = 'api/grafos';

  constructor(private httpClient: HttpClient) {}

  teste(options: OptionLocation[]): Observable<string> {
    return this.httpClient.post<string>(this.API, options);
  }

  autocomplete(name: string): Observable<OptionLocation[]> {
    return this.httpClient.post<OptionLocation[]>(
      this.API + '/autocomplete',
      name
    );
  }
}
