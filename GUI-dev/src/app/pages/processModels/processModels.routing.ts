import { Routes, RouterModule }  from '@angular/router';
import { AuthGuard } from '../../auth.guard';
import { ProcessModels } from './processModels.component';

// noinspection TypeScriptValidateTypes
const routes: Routes = [
  {
    path: '',
    component: ProcessModels,
    canActivate: [AuthGuard],
    children: [
      //{ path: 'treeview', component: TreeViewComponent }
    ]
  }
];

export const routing = RouterModule.forChild(routes);
