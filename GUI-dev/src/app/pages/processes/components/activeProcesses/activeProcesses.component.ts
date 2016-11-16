import {Component} from '@angular/core';
import { ActiveProcessesService } from './activeProcesses.service';

@Component({
  selector: 'activeProcesses',
  styles: [],
  template: `<router-outlet></router-outlet>`
})
export class ActiveProcesses {

  constructor(protected service: ActiveProcessesService) {
  }
}