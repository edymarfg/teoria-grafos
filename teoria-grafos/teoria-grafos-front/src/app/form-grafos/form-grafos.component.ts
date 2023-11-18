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
  optionAtual?: OptionLocation = undefined;
  suggestionsAtual!: OptionLocation[];

  optionDestino?: OptionLocation = undefined;
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

  searchMatrix() {
    if (!this.optionsAll?.length) return;
    this.disparaToast('info', 'Processando', 'Solicitação sendo processada');
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
      });
  }

  searchMatrixAutocomplete(event: AutoCompleteCompleteEvent) {
    this.formGrafosService.autocomplete(event.query).subscribe((it) => {
      this.suggestionsAll = it;
    });
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

  private mountTreeNode() {
    // const mountChildren = (inicio: GrafoMatrixLocations): TreeNode[] => {
    //   inicios.push(inicio.inicio);
    //   let treeNode: TreeNode[] = [];
    //   inicio.fim.forEach((it) => {
    //     if (inicios.includes(it.fim)) {
    //       treeNode.push({
    //         label: `${
    //           it.fim
    //         }\nduração: ${it.duration.toLocaleString()}\ndistância: ${it.distance.toLocaleString()}`,
    //         children: mountChildren(this.findMatrixByName(it.fim)),
    //       });
    //     }
    //   });
    //   return treeNode;
    // };
    this.grafo = [];
    const inicio = this.matrix[0];
    this.grafo.push({
      label: `${inicio.inicio}`,
      expanded: true,
      expandedIcon: undefined,
      children: this.mountChildren(inicio),
    });
    console.log('AQUI O NODE', this.grafo);
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

  private toHours(duration: number) {
    var hours = Math.floor(duration / (60 * 60));

    var divisor_for_minutes = duration % (60 * 60);
    var minutes = Math.floor(divisor_for_minutes / 60);

    var divisor_for_seconds = divisor_for_minutes % 60;
    var seconds = Math.ceil(divisor_for_seconds);
    return `${this.formatNumber(hours)}:${this.formatNumber(
      minutes
    )}:${this.formatNumber(seconds)}`;
    // var obj = {
    //   h: hours,
    //   m: minutes,
    //   s: seconds,
    // };
    // return obj;
    //return duration / 60 / 60;
    // const date = new Date();
    // date.setSeconds(duration);
    // return date.toTimeString();
    // return new Intl.DateTimeFormat('pt-BR', {
    //   hour: '2-digit',
    //   minute: '2-digit',
    //   second: '2-digit',
    // }).format(dura);
    //return duration;
  }

  private formatNumber(num: number): string {
    let strNum = `${num}`;
    if (strNum.length < 2) {
      strNum = `0${strNum}`;
    }
    return strNum;
  }

  private getChild(grafoMatrix: GrafoMatrixLocations): TreeNode[] {
    // let fim = grafoMatrix.fim.filter((it) => it.fim !== this.matrix[0].inicio);
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
