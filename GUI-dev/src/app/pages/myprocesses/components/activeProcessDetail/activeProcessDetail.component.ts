import 'rxjs/add/operator/switchMap';
import { Component,  OnInit } from '@angular/core';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { ProcessesService } from '../../../../Processes.service';
import { BaThemeSpinner } from '../../../../theme/services';

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
      lastChanged: number[],
      receiveState: string,
      ssId: number,
      stateFunctionType: string,
      stateName: string,
      subjectName: string,
      userId: number,
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
    sid:number
  }];


  constructor(protected service: ProcessesService, protected spinner:BaThemeSpinner, protected route: ActivatedRoute, protected router: Router) {
  }

  ngOnInit() {
    var that = this;
    this.spinner.show();
    this.piId = +this.route.snapshot.params['piId'];
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

  logForm(value: any) {
    console.log(value);
  }

  //dirty hack so that the value of the checkbox changes (otherwise the form submit value will stay the original value)
  onChangeCheckboxFn(that, element){
    var name = element.name.split("-:_")[0];
    that.model._parent.form.controls[name].setValue(element.checked)
  }
}
