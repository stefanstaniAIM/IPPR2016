import {Component} from '@angular/core';
import { ProcessesService } from '../../../../Processes.service';

@Component({
  selector: 'terminatedProcesses',
  styles: [],
  template: require('./terminatedProcesses.html')
})
export class TerminatedProcesses {

  constructor(protected service: ProcessesService) {
  }
}
