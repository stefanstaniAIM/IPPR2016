import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { ProcessesService } from '../../Processes.service';
import { ModalModule } from 'ng2-bootstrap/ng2-bootstrap';

@Component({
  selector: 'startableProcesses',
  styles: [require('./startableProcesses.scss')],
  template: require('./startableProcesses.html')
})
export class StartableProcesses implements OnInit {
   processModels = [];
   msg = undefined;
   selectedProcessModel = {name: "Kein Modell ausgewählt"};

  constructor(protected service:ProcessesService) {}

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

  selectProcessModel(pm):void {
    this.selectedProcessModel = pm;
  }

  unselectProcessModel():void {
    this.selectedProcessModel = undefined;
  }

}
