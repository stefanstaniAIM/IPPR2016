import { Component, ViewChild } from '@angular/core';
import { OnInit } from '@angular/core';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { ProcessesService } from '../../../../Processes.service';
//import { ModalModule, ModalDirective } from 'ng2-bootstrap/ng2-bootstrap';

@Component({
  selector: 'startableProcesses',
  styles: [require('./startableProcesses.scss')],
  template: require('./startableProcesses.html')
})
export class StartableProcesses implements OnInit {
   //@ViewChild('lgModal') public modal:ModalDirective;
   processModels = [];
   msg = undefined;
   selectedProcessModel = {name: "Kein Modell ausgewählt"};
   possibleUserAssignments = [];
   selectedUserAssignments = {};
   isSelectionValid = false;

  constructor(protected service:ProcessesService, protected route: ActivatedRoute, protected router: Router) {}

  ngOnInit(): void {
    var that = this;
    this.service.getProcessModels()
      .subscribe(
         data => {
            that.processModels = JSON.parse(data['_body']);
         },
         err => that.msg = {text: err, type: 'error'},
         () => console.log('Request Complete')
       );
  }

  startProcess(pmId:number):void {
    var that = this;
    this.service.startProcess(pmId)
      .subscribe(
        data => {
          that.msg = {text: "Process started", type: 'success'};
          //this.modal.hide();
          var piId = JSON.parse(data['_body']).piId;
          that.router.navigate(['../active', piId], { relativeTo: that.route });
        },
        err =>{
          that.msg = {text: err, type: 'error'}
          //this.modal.hide();
        },
        () => console.log("Request done")
      );
  }
}
