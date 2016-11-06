import { Routes, RouterModule }  from '@angular/router';
import { NotAuthGuard } from '../../notauth.guard';
import { Login } from './login.component';

// noinspection TypeScriptValidateTypes
const routes: Routes = [
  {
    path: '',
    component: Login,
    canActivate: [NotAuthGuard]
  }
];

export const routing = RouterModule.forChild(routes);
