import {Component} from '@angular/core';
import { OnInit } from '@angular/core';
import { StartableProcessesService } from './startableProcesses.service';

@Component({
  selector: 'startableProcesses',
  styles: [],
  template: require('./startableProcesses.html')
})
export class StartableProcesses implements OnInit {

   processModels = [];

  constructor(protected service:StartableProcessesService) {
  }

  ngOnInit(): void {
   this.service.getProcessModels()
      .subscribe(
         data => {
            console.log(data);
            this.processModels = JSON.parse(data['_body']);
         },
         err => console.log('ERROR: '+err),
         () => console.log('Request Complete')
       );
  }

  newProcess(pmId:number):void {
      console.log("start new process: " +pmId);
  }
}
