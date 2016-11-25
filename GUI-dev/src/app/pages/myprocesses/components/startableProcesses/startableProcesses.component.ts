import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { StartableProcessesService } from './startableProcesses.service';

@Component({
  selector: 'startableProcesses',
  styles: [],
  template: require('./startableProcesses.html')
})
export class StartableProcesses implements OnInit {
   processModels = [];
   msg = undefined;

  constructor(protected service:StartableProcessesService) {}

  ngOnInit(): void {
   this.service.getProcessModels()
      .subscribe(
         data => {
            this.processModels = JSON.parse(data['_body']);
         },
         err => this.msg = {text: err, type: 'error'},
         () => console.log('Request Complete')
       );
  }

  startProcess(pmId:number):void {
      this.service.startProcess(pmId)
        .subscribe(
          data => {
            this.msg = {text: "Process started", type: 'success'}
            //auf die Prozessanzeigeseite leiten
          },
          err => this.msg = {text: err, type: 'error'},
          () => console.log('Request Complete')
        );
  }
}
