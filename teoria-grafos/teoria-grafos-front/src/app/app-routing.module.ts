import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FormGrafosComponent } from './form-grafos/form-grafos.component';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'form-grafos' },
  { path: 'form-grafos', component: FormGrafosComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
