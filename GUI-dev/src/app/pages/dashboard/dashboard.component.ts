import {Component, ViewEncapsulation} from '@angular/core';
import { ProcessesService } from '../../Processes.service';

@Component({
  selector: 'dashboard',
  encapsulation: ViewEncapsulation.None,
  styles: [require('./dashboard.scss')],
  template: require('./dashboard.html')
})
export class Dashboard {

  activeProcesses;
  finishedProcesses;
  activeProcesses24Hours;
  finishedProcesses24Hours;

  constructor(private _processService:ProcessesService) {
    var that = this;
    this._processService.getProcessesInState("active")
    .subscribe(
        data => {
          that.activeProcesses = JSON.parse(data['_body']);
        },
        err =>{
          console.log(err);
        }
      );
    this._processService.getProcessesInState("finished")
    .subscribe(
        data => {
          that.finishedProcesses = JSON.parse(data['_body']);
        },
        err =>{
          console.log(err);
        }
      );
    this._processService.getProcessesThatStartedHoursAgo(24)
    .subscribe(
        data => {
          that.activeProcesses24Hours = JSON.parse(data['_body']);
        },
        err =>{
          console.log(err);
        }
      );
      this._processService.getProcessesThatFinishedHoursAgo(24)
      .subscribe(
          data => {
            that.finishedProcesses24Hours = JSON.parse(data['_body']);
          },
          err =>{
            console.log(err);
          }
        );
  }

}
