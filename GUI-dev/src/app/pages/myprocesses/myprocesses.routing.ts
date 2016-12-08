import { Routes, RouterModule }  from '@angular/router';
import { AuthGuard } from '../../auth.guard';
import { MyProcesses } from './myprocesses.component';
import { ActiveProcesses } from './components/activeProcesses/activeProcesses.component';
import { ActiveProcessDetail } from './components/activeProcessDetail/activeProcessDetail.component';
import { TerminatedProcesses } from './components/terminatedProcesses/terminatedProcesses.component';
import { StartableProcesses } from './components/startableProcesses/startableProcesses.component';

// noinspection TypeScriptValidateTypes
const routes: Routes = [
  {
    path: '',
    component: MyProcesses,
    canActivate: [AuthGuard],
    children: [
      { path: 'active', component: ActiveProcesses },
      { path: 'active/:piId', component: ActiveProcessDetail },
      { path: 'terminated', component: TerminatedProcesses },
      { path: 'startable', component: StartableProcesses }
    ]
  }
];

export const routing = RouterModule.forChild(routes);
