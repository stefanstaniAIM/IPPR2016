import {Component} from '@angular/core';
import { TerminatedProcessesService } from './terminatedProcesses.service';

@Component({
  selector: 'terminatedProcesses',
  styles: [],
  template: require('./terminatedProcesses.html')
})
export class TerminatedProcesses {

  constructor(protected service: TerminatedProcessesService) {
  }
}
