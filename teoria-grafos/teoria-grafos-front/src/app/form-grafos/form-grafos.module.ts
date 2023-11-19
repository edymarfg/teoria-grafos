import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { ButtonModule } from 'primeng/button';
import { OrganizationChartModule } from 'primeng/organizationchart';
import { ToastModule } from 'primeng/toast';
import { TooltipModule } from 'primeng/tooltip';
import { TreeSelectModule } from 'primeng/treeselect';
import { FormGrafosComponent } from './form-grafos.component';
import { FormGrafosService } from './services/form-grafos.service';

import { DividerModule } from 'primeng/divider';
import { TableModule } from 'primeng/table';
import { DropdownModule } from '../dropdown/dropdown.module';

@NgModule({
  declarations: [FormGrafosComponent],
  imports: [
    CommonModule,
    ButtonModule,
    AutoCompleteModule,
    FormsModule,
    ToastModule,
    OrganizationChartModule,
    TreeSelectModule,
    TooltipModule,
    TableModule,
    DividerModule,
    DropdownModule,
  ],
  providers: [FormGrafosService, MessageService],
})
export class FormGrafosModule {}
