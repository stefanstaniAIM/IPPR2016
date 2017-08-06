import { NgModule }      from '@angular/core';
import { CommonModule }  from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgaModule } from '../../theme/nga.module';

import { Admin } from './admin.component';
import { routing }       from './admin.routing';
import { Pipes }       from '../../pipes/pipes.module';

import { ActiveProcesses } from './components/activeProcesses/activeProcesses.component';
import { ImportProcessModel } from './components/importProcessModel/importProcessModel.component';
import { TerminatedProcesses } from './components/terminatedProcesses/terminatedProcesses.component';
import { ProcessModels } from './components/processModels/processModels.component';
import { EventLogger } from './components/eventLogger/eventLogger.component';
import { ManipulatePNML } from './components/manipulatePNML/manipulatePNML.component';
import { GenerateOWL } from './components/generateOWL/generateOWL.component';

import { AuthGuard } from '../../auth.guard';
import { AdminGuard } from '../../admin.guard';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NgaModule,
    routing,
    Pipes,
  ],
  declarations: [
    Admin,
    ActiveProcesses,
    TerminatedProcesses,
    ProcessModels,
    ImportProcessModel,
    EventLogger,
    ManipulatePNML,
    GenerateOWL
  ],
  providers: [
    AuthGuard,
    AdminGuard
  ]
})
export default class AdminModule {}
