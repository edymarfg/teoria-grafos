import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { ListboxModule } from 'primeng/listbox';
import { DropdownComponent } from './dropdown.component';

@NgModule({
  declarations: [DropdownComponent],
  imports: [
    CommonModule,
    ListboxModule,
    InputTextModule,
    ButtonModule,
    FormsModule,
  ],
  exports: [DropdownComponent],
})
export class DropdownModule {}
