import { Routes, RouterModule }  from '@angular/router';
import { AuthGuard } from '../../auth.guard';
import { Dashboard } from './dashboard.component';

// noinspection TypeScriptValidateTypes
const routes: Routes = [
  {
    path: '',
    component: Dashboard,
    canActivate: [AuthGuard],
    children: [
      //{ path: 'treeview', component: TreeViewComponent }
    ]
  }
];

export const routing = RouterModule.forChild(routes);
