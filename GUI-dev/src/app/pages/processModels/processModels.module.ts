import { NgModule }      from '@angular/core';
import { CommonModule }  from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgaModule } from '../../theme/nga.module';

import { ProcessModels } from './processModels.component';
import { ProcessModelsService } from './processModels.service';
import { routing }       from './processModels.routing';

import { AuthGuard } from '../../auth.guard';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NgaModule,
    routing
  ],
  declarations: [
    ProcessModels,
  ],
  providers: [
    AuthGuard,
    ProcessModelsService
  ]
})
export default class ProcessModelsModule {}
