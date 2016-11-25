import {Component} from '@angular/core';
import { ProcessesService } from '../../Processes.service';

@Component({
  selector: 'activeProcesses',
  styles: [],
  template:  require('./activeProcesses.html')
})
export class ActiveProcesses {

  constructor(protected service: ProcessesService) {
  }
}
