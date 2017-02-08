import { NgModule }      from '@angular/core';
import { CommonModule }  from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgaModule } from '../../theme/nga.module';

import { Dashboard } from './dashboard.component';
import { routing }       from './dashboard.routing';

import { LineChart } from './lineChart';
import { Todo } from './todo';
import { LineChartService } from './lineChart/lineChart.service';
import { AuthGuard } from '../../auth.guard';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NgaModule,
    routing
  ],
  declarations: [
    LineChart,
    Todo,
    Dashboard,
  ],
  providers: [
    LineChartService,
    AuthGuard
  ]
})
export default class DashboardModule {}
