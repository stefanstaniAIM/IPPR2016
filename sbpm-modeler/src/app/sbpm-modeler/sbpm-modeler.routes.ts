import { Routes, RouterModule }  from '@angular/router';
import { SbpmModelerComponent } from './sbpm-modeler.component';

const ROUTES: Routes = [
  { path: 'sbpm-modeler', component: SbpmModelerComponent},
];

export const ROUTING = RouterModule.forChild(ROUTES);