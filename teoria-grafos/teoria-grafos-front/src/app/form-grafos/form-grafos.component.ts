import { Component, ViewChild } from '@angular/core';
import { MessageService, TreeNode } from 'primeng/api';
import { AutoComplete, AutoCompleteCompleteEvent } from 'primeng/autocomplete';
import { OptionLocation } from './model/OptionLocation';
import { FormGrafosService } from './services/form-grafos.service';
import {
  GrafoMatrixLocations,
  PesoGrafoMatrix,
} from './model/GrafoMatrixLocations';
import {
  CategoriaProfile,
  categoriaProfileOptions,
} from './model/CategoriaProfile';

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

  optionAtual: OptionLocation = { label: '', value: [] };
  optionDestino: OptionLocation = { label: '', value: [] };
  suggestions!: OptionLocation[];

  suggestionsDestino!: OptionLocation[];

  optionsAll?: OptionLocation[];
  suggestionsAll!: OptionLocation[];

  optionCategoriaProfile: CategoriaProfile = categoriaProfileOptions[0];
  suggestionsCategoriaProfile = categoriaProfileOptions;

  optAtuDisabled = false;
  optDestDisabled = false;
  grafo: TreeNode[] = [];
  matrix: GrafoMatrixLocations[] = [];
  constructor(
    private readonly formGrafosService: FormGrafosService,
    private messageService: MessageService
  ) {}

  onClick() {
    if (!this.optionAtual || !this.optionDestino) return;
    this.toastProcessando;
    this.formGrafosService
      .teste([this.optionAtual, this.optionDestino])
      .subscribe({
        next: (it) => {
          console.log(it);
          this.teste = it;
          this.toastSucesso;
        },
        error: (err) => {
          this.toastErro(err);
        },
      })
      .add(() => this.limpaSelected());
  }

  searchMatrix() {
    if (!this.optionsAll?.length) return;
    this.toastProcessando();
    this.formGrafosService
      .matrix({
        optns: this.optionsAll,
        categoria: this.optionCategoriaProfile.value,
      })
      .subscribe({
        next: (it) => {
          console.log(it);

          this.teste = JSON.stringify(it);
          this.matrix = it;
          this.mountTreeNode();
          this.toastSucesso();
          this.suggestions = this.optionsAll ?? [];
        },
        error: (err) => {
          this.toastErro(err);
        },
      });
  }

  searchMatrixAutocomplete(event: AutoCompleteCompleteEvent) {
    this.formGrafosService.autocomplete(event.query).subscribe((it) => {
      this.suggestionsAll = it;
    });
  }

  searchAtual(event: AutoCompleteCompleteEvent) {
    // if (!event.query) {
    //   this.suggestionsAtual = [];
    //   return;
    // }
    // this.optAtuDisabled = true;
    // this.formGrafosService.autocomplete(event.query).subscribe((it) => {
    //   this.suggestionsAtual = it;
    //   console.log(this.suggestionsAtual);
    //   this.optAtuDisabled = false;
    //   this.autocompleteatual.autofocus = true;
    //   this.autocompleteatual.overlayVisible = true;
    // });
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
    return !this.optionAtual.value.length || !this.optionDestino.value.length;
  }

  private mountTreeNode() {
    this.grafo = [];
    const inicio = this.matrix[0];
    this.grafo.push({
      label: `${inicio.inicio}`,
      expanded: true,
      expandedIcon: undefined,
      children: this.mountChildren(inicio),
    });
  }

  private mountChildren(inicio: GrafoMatrixLocations): TreeNode[] {
    return inicio.fim.map((it) => {
      return {
        label: `${it.fim}\nduração: ${this.toHours(it.duration)}\ndistância: ${
          it.distance
        }Km`,
        expanded: true,
        expandedIcon: undefined,
        children: this.getChild(this.findMatrixByName(it.fim)),
      };
    });
  }

  toHours(duration: number) {
    var hours = Math.floor(duration / (60 * 60));

    var divisor_for_minutes = duration % (60 * 60);
    var minutes = Math.floor(divisor_for_minutes / 60);

    var divisor_for_seconds = divisor_for_minutes % 60;
    var seconds = Math.ceil(divisor_for_seconds);
    return `${this.formatNumber(hours)}:${this.formatNumber(
      minutes
    )}:${this.formatNumber(seconds)}`;
  }

  private formatNumber(num: number): string {
    let strNum = `${num}`;
    if (strNum.length < 2) {
      strNum = `0${strNum}`;
    }
    return strNum;
  }

  private getChild(grafoMatrix: GrafoMatrixLocations): TreeNode[] {
    //let fim = grafoMatrix.fim.filter((it) => it.fim !== this.matrix[0].inicio);
    return grafoMatrix.fim.map((it) => {
      return {
        label: `${it.fim}\nduração: ${this.toHours(
          it.duration
        ).toLocaleString()}\ndistância: ${it.distance.toLocaleString()}Km`,
      };
    });
  }

  private findMatrixByName(name: string): GrafoMatrixLocations {
    let mat = this.matrix.filter((it) => it.inicio === name)[0];
    // mat.fim.filter((it) => !exclude.includes(it.fim));
    return mat;
  }

  private limpaSelected() {
    this.optionAtual = { label: '', value: [] };
    this.optionDestino = { label: '', value: [] };
  }

  private toastProcessando() {
    this.disparaToast('info', 'Processando', 'Solicitação sendo processada');
  }

  private toastSucesso() {
    this.disparaToast(
      'success',
      'Sucesso',
      'A solicitação foi realizada com sucesso'
    );
  }

  private toastErro(err: string) {
    this.disparaToast(
      'error',
      'Erro',
      'Ocorreu um erro na solicitação: ' + err
    );
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
