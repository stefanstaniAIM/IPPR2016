import { NgModule }      from '@angular/core';
import { CommonModule }  from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgaModule } from '../../theme/nga.module';

import { Admin } from './admin.component';
import { routing }       from './admin.routing';

import { ActiveProcesses } from './components/activeProcesses/activeProcesses.component';
import { TerminatedProcesses } from './components/terminatedProcesses/terminatedProcesses.component';
import { ProcessModels } from './components/processModels/processModels.component';
import { ActiveProcessesService } from './components/activeProcesses/activeProcesses.service';
import { TerminatedProcessesService } from './components/terminatedProcesses/terminatedProcesses.service';
import { ProcessModelsService } from './components/processModels/processModels.service';

import { AuthGuard } from '../../auth.guard';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NgaModule,
    routing
  ],
  declarations: [
    Admin,
    ActiveProcesses,
    TerminatedProcesses,
    ProcessModels
  ],
  providers: [
    AuthGuard,
    ActiveProcessesService,
    TerminatedProcessesService,
    ProcessModelsService
  ]
})
export default class AdminModule {}
