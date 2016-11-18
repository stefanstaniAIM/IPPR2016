import { NgModule }      from '@angular/core';
import { CommonModule }  from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgaModule } from '../../theme/nga.module';

import { Processes } from './processes.component';
import { routing }       from './processes.routing';

import { ActiveProcesses } from './components/activeProcesses/activeProcesses.component';
import { TerminatedProcesses } from './components/terminatedProcesses/terminatedProcesses.component';
import { StartableProcesses } from './components/startableProcesses/startableProcesses.component';
import { ActiveProcessesService } from './components/activeProcesses/activeProcesses.service';
import { TerminatedProcessesService } from './components/terminatedProcesses/terminatedProcesses.service';
import { StartableProcessesService } from './components/startableProcesses/startableProcesses.service';

import { AuthGuard } from '../../auth.guard';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NgaModule,
    routing
  ],
  declarations: [
    Processes,
    ActiveProcesses,
    TerminatedProcesses,
    StartableProcesses
  ],
  providers: [
    AuthGuard,
    ActiveProcessesService,
    TerminatedProcessesService,
    StartableProcessesService
  ]
})
export default class ProcessesModule {}
