import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { MessageService, TreeNode } from 'primeng/api';
import { AutoComplete, AutoCompleteCompleteEvent } from 'primeng/autocomplete';
import { OptionLocation } from './model/OptionLocation';
import { FormGrafosService } from './services/form-grafos.service';
import { GrafoMatrixLocations } from './model/GrafoMatrixLocations';
import {
  CategoriaProfile,
  categoriaProfileOptions,
} from './model/CategoriaProfile';
import { Step } from './model/Step';
import * as L from 'leaflet';

type severity = 'success' | 'info' | 'warn' | 'error';
type summary = 'Sucesso' | 'Processando' | 'Atenção' | 'Erro';
const iconRetinaUrl = 'assets/marker-icon-2x.png';
const iconUrl = 'assets/marker-icon.png';
const shadowUrl = 'assets/marker-shadow.png';
const iconDefault = L.icon({
  iconRetinaUrl,
  iconUrl,
  shadowUrl,
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  tooltipAnchor: [16, -28],
  shadowSize: [41, 41],
});
L.Marker.prototype.options.icon = iconDefault;

@Component({
  selector: 'app-form-grafos',
  templateUrl: './form-grafos.component.html',
  styleUrls: ['./form-grafos.component.scss'],
})
export class FormGrafosComponent implements AfterViewInit {
  @ViewChild('autocompleteatual') autocompleteatual!: AutoComplete;
  @ViewChild('autocompletedestino') autocompletedestino!: AutoComplete;
  teste!: string;

  optionAtual: OptionLocation = { label: '', value: [] };
  optionDestino: OptionLocation = { label: '', value: [] };
  suggestions!: OptionLocation[];

  // suggestionsDestino!: OptionLocation[];

  optionsAll?: OptionLocation[];
  suggestionsAll!: OptionLocation[];

  optionCategoriaProfile: CategoriaProfile = categoriaProfileOptions[0];
  suggestionsCategoriaProfile = categoriaProfileOptions;

  private map!: L.Map;
  private coord = [51.505, -0.09];

  optAtuDisabled = false;
  optDestDisabled = false;
  showCategoriaDropdown = false;
  grafo: TreeNode[] = [];
  matrix: GrafoMatrixLocations[] = [];
  rotaPerson: Step[] = [];

  constructor(
    private readonly formGrafosService: FormGrafosService,
    private messageService: MessageService
  ) {}

  ngAfterViewInit(): void {
    this.creatMap({ label: 'te', value: [-47.9292, -15.7801] }, 5);
  }

  onSearchSteps() {
    if (!this.optionAtual || !this.optionDestino) return;
    this.toastProcessando();
    this.formGrafosService
      .rotas({
        optns: [this.optionAtual, this.optionDestino],
        categoria: this.optionCategoriaProfile.value,
      })
      .subscribe({
        next: (it) => {
          this.rotaPerson = it;
          this.initMap([this.optionAtual, this.optionDestino]);
          this.toastSucesso();
        },
        error: (err) => {
          this.toastErro(err);
        },
      });
  }

  searchMatrix() {
    if (!this.optionsAll?.length) return;
    this.rotaPerson = [];
    this.showCategoriaDropdown = false;
    this.toastProcessando();
    this.formGrafosService
      .matrix({
        optns: this.optionsAll,
        categoria: this.optionCategoriaProfile.value,
      })
      .subscribe({
        next: (it) => {
          this.teste = JSON.stringify(it);
          this.matrix = it;
          this.mountTreeNode();
          this.initMap(this.optionsAll ?? []);
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

  creatMap(option: OptionLocation, zoom: number): void {
    this.map = L.map('map', {
      center: this.turnValues(option.value),
      zoom: zoom,
    });

    const tiles = L.tileLayer(
      'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
      {
        maxZoom: 18,
        minZoom: 3,
        attribution:
          '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>',
      }
    );

    tiles.addTo(this.map);
  }

  initMap(options: OptionLocation[]): void {
    if (this.map) {
      this.map.remove();
    }
    this.creatMap(options[0], 7);
    this.makeMarkers(options);
  }

  makeMarkers(options: OptionLocation[]): void {
    options.forEach((opt) => {
      const values = this.turnValues(opt.value);
      const mark = L.marker(values).addTo(this.map);

      if (options.length === 2) {
        mark
          .bindPopup(this.makePopup(options[0].label, options[1].label))
          .openPopup();
      }

      mark.addTo(this.map);
    });

    const coord = this.connectTheDots(options);

    setTimeout(() => L.polyline(coord).addTo(this.map));
  }

  makePopup(inicio: string, fim: string): string {
    const d = this.findDistanceAndDurationByName(inicio, fim);
    console.log(d);

    if (!d) return '';
    return (
      `` +
      `<div>${fim}</div>` +
      `<div>Distância: ${d[0]}Km</div>` +
      `<div>Duração: ${this.toHours(d[1])}</div>`
    );
  }

  connectTheDots(options: OptionLocation[]): L.LatLngExpression[] {
    let c: L.LatLngExpression[] = [];
    options.forEach((it) => {
      const opt = options.filter((op) => op.label != it.label);
      opt.forEach((op) => {
        c.push(this.turnValues(it.value));
        c.push(this.turnValues(op.value));
      });
    });
    return c;
  }

  findDistanceAndDurationByName(
    inicio: string,
    fim: string
  ): number[] | undefined {
    const t = this.matrix.find((it) => it.inicio === inicio);
    if (t) {
      const f = t.fim.find((it) => it.fim === fim);
      return f ? [f.distance, f.duration] : undefined;
    }
    return undefined;
  }

  getDisableButton(): boolean {
    return !this.optionAtual.value.length || !this.optionDestino.value.length;
  }

  toKm(distance: number): string {
    return (distance / 1000).toFixed(2);
  }

  toHours(duration: number): string {
    var hours = Math.floor(duration / (60 * 60));

    var divisor_for_minutes = duration % (60 * 60);
    var minutes = Math.floor(divisor_for_minutes / 60);

    var divisor_for_seconds = divisor_for_minutes % 60;
    var seconds = Math.ceil(divisor_for_seconds);
    return `${this.formatNumber(hours)}:${this.formatNumber(
      minutes
    )}:${this.formatNumber(seconds)}`;
  }

  private mountTreeNode(): void {
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

  private turnValues(values: number[]): L.LatLngExpression {
    return [values[1], values[0]] as L.LatLngExpression;
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

  // searchAtual(event: AutoCompleteCompleteEvent) {
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
  // }

  // searchDestino(event: AutoCompleteCompleteEvent) {
  //   if (!event.query) {
  //     this.suggestionsDestino = [];
  //     return;
  //   }
  //   this.optDestDisabled = true;
  //   this.formGrafosService.autocomplete(event.query).subscribe((it) => {
  //     this.suggestionsDestino = it;
  //     console.log(this.suggestionsDestino);
  //     this.optDestDisabled = false;
  //     this.autocompletedestino.autofocus = true;
  //     this.autocompletedestino.overlayVisible = true;
  //   });
  // }
}
