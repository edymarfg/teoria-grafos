import { MessageService } from 'primeng/api';
import { Component, ViewChild } from '@angular/core';
import { FormGrafosService } from './services/form-grafos.service';
import { AutoComplete, AutoCompleteCompleteEvent } from 'primeng/autocomplete';
import { OptionLocation } from './model/OptionLocation';

export type severity = 'success' | 'info' | 'warn' | 'error';
export type summary = 'Sucesso' | 'Processando' | 'Atenção' | 'Erro';

@Component({
  selector: 'app-form-grafos',
  templateUrl: './form-grafos.component.html',
  styleUrls: ['./form-grafos.component.scss'],
})
export class FormGrafosComponent {
  @ViewChild('autocompleteatual') autocompleteatual!: AutoComplete;
  @ViewChild('autocompletedestino') autocompletedestino!: AutoComplete;
  teste!: string;
  optionAtual?: OptionLocation = undefined;
  suggestionsAtual!: OptionLocation[];

  optionDestino?: OptionLocation = undefined;
  suggestionsDestino!: OptionLocation[];

  optAtuDisabled = false;
  optDestDisabled = false;
  constructor(
    private readonly formGrafosService: FormGrafosService,
    private messageService: MessageService
  ) {}

  onClick() {
    if (!this.optionAtual || !this.optionDestino) return;
    this.disparaToast('info', 'Processando', 'Solicitação sendo processada');
    this.formGrafosService
      .teste([this.optionAtual, this.optionDestino])
      .subscribe({
        next: (it) => {
          console.log(it);
          this.teste = it;
          this.disparaToast(
            'success',
            'Sucesso',
            'A solicitação foi realizada com sucesso'
          );
        },
        error: (err) => {
          this.disparaToast(
            'error',
            'Erro',
            'Ocorreu um erro na solicitação: ' + err
          );
        },
      })
      .add(() => this.limpaSelected());
  }

  searchAtual(event: AutoCompleteCompleteEvent) {
    if (!event.query) {
      this.suggestionsAtual = [];
      return;
    }
    this.optAtuDisabled = true;
    this.formGrafosService.autocomplete(event.query).subscribe((it) => {
      this.suggestionsAtual = it;
      console.log(this.suggestionsAtual);
      this.optAtuDisabled = false;
      this.autocompleteatual.autofocus = true;
      this.autocompleteatual.overlayVisible = true;
    });
  }

  searchDestino(event: AutoCompleteCompleteEvent) {
    if (!event.query) {
      this.suggestionsDestino = [];
      return;
    }
    this.optDestDisabled = true;
    this.formGrafosService.autocomplete(event.query).subscribe((it) => {
      this.suggestionsDestino = it;
      console.log(this.suggestionsDestino);
      this.optDestDisabled = false;
      this.autocompletedestino.autofocus = true;
      this.autocompletedestino.overlayVisible = true;
    });
  }

  getDisableButton(): boolean {
    return !this.optionAtual || !this.optionDestino;
  }

  private limpaSelected() {
    this.optionAtual = undefined;
    this.optionDestino = undefined;
  }

  private disparaToast(
    severity: severity,
    summary: summary,
    message: string
  ): void {
    this.messageService.add({
      severity: severity,
      summary: summary,
      detail: message,
    });
  }
}
