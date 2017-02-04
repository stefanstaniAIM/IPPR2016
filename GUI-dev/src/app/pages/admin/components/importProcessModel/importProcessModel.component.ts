import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { ProcessesService } from '../../../../Processes.service';

@Component({
  selector: 'import',
  styles: [],
  template: require('./importProcessModel.html')
})
export class ImportProcessModel implements OnInit {
   processModel;
   rules;
   error = undefined;

  constructor(protected service:ProcessesService) {}

  ngOnInit(): void {
   /*this.service.getProcessModels()
      .subscribe(
         data => {
            console.log(data);
            this.processModels = JSON.parse(data['_body']);
         },
         err => this.error = err,
         () => console.log('Request Complete')
       );*/
  }

  uploadOWLModel(form):void {
    var that = this;
    console.log("upload owl model");
    this.service.uploadOWLModel(null)
       .subscribe(
          data => {
             console.log(data);
             that.processModel = JSON.parse(data['_body']);
             that.initRules();
          },
          err => this.error = err,
          () => console.log('Request Complete')
        );
  }

  initRules():void{
    var that = this;
    this.service.getRules()
       .subscribe(
          data => {
             console.log(data);
             that.rules = JSON.parse(data['_body']);
          },
          err => this.error = err,
          () => console.log('Request Complete')
        );
  }

  uploadProcessModel(form):void {
    var that = this;
    console.log(form);
  }
}
