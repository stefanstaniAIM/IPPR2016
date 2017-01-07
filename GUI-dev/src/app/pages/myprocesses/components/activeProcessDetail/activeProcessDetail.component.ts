import 'rxjs/add/operator/switchMap';
import { Component,  OnInit } from '@angular/core';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { ProcessesService } from '../../../../Processes.service';
import { BaThemeSpinner } from '../../../../theme/services';
import { User } from '../../../../user';

@Component({
  selector: 'activeProcessDetail',
  styles: [],
  template:  require('./activeProcessDetail.html')
})
export class ActiveProcessDetail implements OnInit {

  piId:number;
  msg = undefined;
  subjectsState:{
    piId:number,
    status: string,
    startTime: number[],
    endTime: number[],
    subjects: [{
      ssId: number,
      userId: number,
      subjectName: string,
      stateName: string,
      stateFunctionType: string,
      subState: string,
      lastChanged: number[],
      user: any
    }]
  };
  businessObjectTemplate = {
    fields:[
      {
        type: "text",
        name: "Textfeld",
        description: "Textfeld für XY",
        value: "Default Value",
        minlength: 5,
        maxlength: 20,
        required: true,
        readonly: false
      },
      {
        type: "number",
        name: "Nummernfeld",
        description: "Nummernfeld für XY",
        value: 123,
        min: 5,
        max: 200,
        required: true,
        readonly: false
      },
      {
        type: "checkbox",
        name: "Checkbox",
        description: "Checkbox für XY",
        value: true,
        readonly: false
      },
      {
        type: "radio",
        name: "Radiofield",
        description: "Radiofield für XY",
        value: "radio1",
        readonly: true,
        choices: [
          {
            name: 'radio1',
            description: 'Radio1'
          }, {
            name: 'radio2',
            description: 'Radio2'
          }, {
            name: 'radio3',
            description: 'Radio3'
          }
        ]
      }
    ]
  }

  businessObjects:[{
    bomId:number,
    boiId:number,
    name:string,
    fields:[{
      bofmId:number,
      bofiId:number,
      name:string,
      type:string,
      required:boolean,
      readonly:boolean,
      value:any
    }]
  }];
  nextStates:[{
    name:string,
    nextStateId:number
  }];

  constructor(protected service: ProcessesService, protected spinner:BaThemeSpinner, protected route: ActivatedRoute, protected router: Router, private _user:User) {
  }

  ngOnInit() {
    var that = this;
    this.spinner.show();
    this.piId = +this.route.snapshot.params['piId'];
    this.businessObjects = undefined;
    this.nextStates = undefined;
    //this.route.params
    //.switchMap((params: Params) => service.loadprocess etc. +params['piId'])
    //.subscribe((piId:number) => this.piId = piId)
    this.service.getProcessState(this.piId)
    .subscribe(
        data => {
          that.subjectsState = JSON.parse(data['_body']);
          that.spinner.hide();
        },
        err =>{
          that.msg = {text: err, type: 'error'}
          console.log(err);
          that.spinner.hide();
        }
      );

      this.service.getTasksForProcessForUser(this.piId)
      .subscribe(
          data => {
            var dataJson = JSON.parse(data['_body']);
            that.businessObjects = dataJson.businessObjects;
            that.nextStates = dataJson.nextStates;
          },
          err =>{
            that.msg = {text: err, type: 'error'}
            console.log(err);
          }
        );
  }

  submitForm(form) {
    var that = this;
    var businessObjectsValues = [];
    this.businessObjects.forEach(bo => {
      var fields = []
      var keys = Object.keys(form.value).forEach(k => {
        var kSplit = k.split("-:_");
        var bomId = kSplit[0];
        var bofmId = kSplit[1];
        if(bomId === (bo.bomId).toString()) {
            var value = form.value[k];
            fields.push({bofmId:bofmId, value:value});
        }
      });
      businessObjectsValues.push({bomId:bo.bomId, fields:fields});
    });
    var businessObjectsAndNextState = {
      nextStateId: form.nextStateId,
      businessObjects: businessObjectsValues
    };
    this.service.submitBusinessObjectsAndNextState(this.piId, businessObjectsAndNextState)
    .subscribe(
        data => {
          console.log(data);
          that.ngOnInit();
        },
        err =>{
          that.msg = {text: err, type: 'error'}
          console.log(err);
        }
      );
    console.log(businessObjectsAndNextState);
  }

  //dirty hack so that the value of the checkbox changes (otherwise the form submit value will stay the original value)
  onChangeCheckboxFn(that, element){
    var name = element.name.split("-:_")[0];
    that.model._parent.form.controls[name].setValue(element.checked)
  }

  isFunctionState(){
    if(this.subjectsState){
      return this.subjectsState.subjects.filter(s => s.userId === this._user.getUid())[0].stateFunctionType === "FUNCTION";
    } else {
      return false;
    }
  }
}
