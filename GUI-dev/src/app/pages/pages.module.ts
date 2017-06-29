import { NgModule }      from '@angular/core';
import { CommonModule }  from '@angular/common';

import { routing }       from './pages.routing';
import { NgaModule } from '../theme/nga.module';

import { Pages } from './pages.component';
import { ProcessesService } from '../Processes.service';
import { EventLoggerService } from '../EventLogger.service';

@NgModule({
  imports: [CommonModule, NgaModule, routing],
  declarations: [Pages],
  providers: [ProcessesService, EventLoggerService]
})
export class PagesModule {
}
