import { Routes, RouterModule }  from '@angular/router';
import { AuthGuard } from '../../auth.guard';
import { Processes } from './processes.component';
import { ActiveProcesses } from './components/activeProcesses/activeProcesses.component';
import { TerminatedProcesses } from './components/terminatedProcesses/terminatedProcesses.component';

// noinspection TypeScriptValidateTypes
const routes: Routes = [
  {
    path: '',
    component: Processes,
    canActivate: [AuthGuard],
    children: [
      { path: 'active', component: ActiveProcesses },
      { path: 'terminated', component: TerminatedProcesses }
    ]
  }
];

export const routing = RouterModule.forChild(routes);
