import { Component, ViewChild } from '@angular/core';
import { OnInit } from '@angular/core';
import { ProcessesService } from '../../Processes.service';
import { ModalModule, ModalDirective } from 'ng2-bootstrap/ng2-bootstrap';

@Component({
  selector: 'startableProcesses',
  styles: [require('./startableProcesses.scss')],
  template: require('./startableProcesses.html')
})
export class StartableProcesses implements OnInit {
   @ViewChild('lgModal') public modal:ModalDirective;
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
            this.msg = {text: "Process started", type: 'success'};
            this.modal.hide();
            //auf die Prozessanzeigeseite leiten
          },
          err =>{
            this.msg = {text: err, type: 'error'}
            this.modal.hide();
          },
          () => console.log("Request done")
        );
  }

  selectProcessModel(pm):void {
    this.selectedProcessModel = pm;
    this.modal.show()
  }

  unselectProcessModel():void {
    this.selectedProcessModel = {name: "Kein Prozess ausgewählt"};
    this.modal.hide();
  }

}
