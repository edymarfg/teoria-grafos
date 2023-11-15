import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { ButtonModule } from 'primeng/button';
import { FormGrafosComponent } from './form-grafos.component';
import { FormGrafosService } from './services/form-grafos.service';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';

@NgModule({
  declarations: [FormGrafosComponent],
  imports: [
    CommonModule,
    ButtonModule,
    AutoCompleteModule,
    FormsModule,
    ToastModule,
  ],
  providers: [FormGrafosService, MessageService],
})
export class FormGrafosModule {}
