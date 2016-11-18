import {Component} from '@angular/core';
import { ActiveProcessesService } from './activeProcesses.service';

@Component({
  selector: 'activeProcesses',
  styles: [],
  template:  require('./activeProcesses.html')
})
export class ActiveProcesses {

  constructor(protected service: ActiveProcessesService) {
  }
}
