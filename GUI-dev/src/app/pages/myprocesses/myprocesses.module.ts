import { NgModule }      from '@angular/core';
import { CommonModule }  from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgaModule } from '../../theme/nga.module';
import { ModalModule } from 'ng2-bootstrap/ng2-bootstrap';

import { MyProcesses } from './myprocesses.component';
import { routing }       from './myprocesses.routing';
import { Pipes }       from '../../pipes/pipes.module';

import { ActiveProcesses } from './components/activeProcesses/activeProcesses.component';
import { ActiveProcessDetail } from './components/activeProcessDetail/activeProcessDetail.component';
import { TerminatedProcesses } from './components/terminatedProcesses/terminatedProcesses.component';
import { StartableProcesses } from './components/startableProcesses/startableProcesses.component';
import { BusinessObjects } from './components/activeProcessDetail/businessObjects.component';


import { AuthGuard } from '../../auth.guard';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NgaModule,
    routing,
    ModalModule,
    Pipes
  ],
  declarations: [
    MyProcesses,
    ActiveProcesses,
    ActiveProcessDetail,
    TerminatedProcesses,
    StartableProcesses,
    BusinessObjects
  ],
  providers: [
    AuthGuard
  ]
})
export default class MyProcessesModule {}
