import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-dropdown',
  templateUrl: './dropdown.component.html',
  styleUrls: ['./dropdown.component.scss'],
})
export class DropdownComponent {
  @Input() suggestions!: any[];
  @Input() option!: any;
  @Input() placeholder: string = '';
  @Input() isShowDropdown: boolean = false;
  @Output() optionChange = new EventEmitter<any>();
}
