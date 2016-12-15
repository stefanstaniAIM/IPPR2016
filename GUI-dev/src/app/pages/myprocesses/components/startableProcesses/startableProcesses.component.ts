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
          //this.modal.hide();
          var piId = JSON.parse(data['_body']).piId;
          this.router.navigate(['../active', piId], { relativeTo: this.route });
        },
        err =>{
          this.msg = {text: err, type: 'error'}
          //this.modal.hide();
        },
        () => console.log("Request done")
      );
  }

  /*selectProcessModel(pm):void {
    var that = this;
    this.isSelectionValid = false;
    this.possibleUserAssignments = [];
    this.selectedProcessModel = pm;
    const subjectModels = pm.subjectModels;
    subjectModels.forEach(
      sm => {
        that.service.getPossibleUsersForProcessModel(sm.group).
        subscribe(
          data => {
                let users = JSON.parse(data['_body']);
                this.possibleUserAssignments.push({groupName: sm.group, smId: sm.smId, users: users});
                this.selectedUserAssignments[sm.group] = undefined;
                this.modal.show();
                console.log(this.possibleUserAssignments);
          },
          err =>{
            this.msg = {text: err, type: 'error'}
            this.possibleUserAssignments = [];
            this.modal.hide();
          },
          () => console.log("Request done")
        );
      });
  }

  unselectProcessModel():void {
    this.selectedProcessModel = {name: "Kein Prozess ausgewählt"};
    this.possibleUserAssignments = [];
    this.isSelectionValid = false;
    this.modal.hide();
  }*/

}
