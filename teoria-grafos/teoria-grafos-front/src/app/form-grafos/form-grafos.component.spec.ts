import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormGrafosComponent } from './form-grafos.component';

describe('FormGrafosComponent', () => {
  let component: FormGrafosComponent;
  let fixture: ComponentFixture<FormGrafosComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FormGrafosComponent]
    });
    fixture = TestBed.createComponent(FormGrafosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
