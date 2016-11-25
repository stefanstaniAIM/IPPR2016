import { NgModule }      from '@angular/core';
import { CommonModule }  from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgaModule } from '../../theme/nga.module';
import { ModalModule } from 'ng2-bootstrap/ng2-bootstrap';

import { MyProcesses } from './myprocesses.component';
import { routing }       from './myprocesses.routing';
import { Pipes }       from '../../pipes/pipes.module';

import { ActiveProcesses } from './components/activeProcesses/activeProcesses.component';
import { TerminatedProcesses } from './components/terminatedProcesses/terminatedProcesses.component';
import { StartableProcesses } from './components/startableProcesses/startableProcesses.component';
import { ProcessesService } from './Processes.service';

import { AuthGuard } from '../../auth.guard';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NgaModule,
    routing,
    Pipes,
    ModalModule
  ],
  declarations: [
    MyProcesses,
    ActiveProcesses,
    TerminatedProcesses,
    StartableProcesses
  ],
  providers: [
    AuthGuard,
    ProcessesService
  ]
})
export default class MyProcessesModule {}
