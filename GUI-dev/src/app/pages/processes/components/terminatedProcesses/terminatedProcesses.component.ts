import {Component} from '@angular/core';
import { TerminatedProcessesService } from './terminatedProcesses.service';

@Component({
  selector: 'terminatedProcesses',
  styles: [],
  template: `<router-outlet></router-outlet>`
})
export class TerminatedProcesses {

  constructor(protected service: TerminatedProcessesService) {
  }
}