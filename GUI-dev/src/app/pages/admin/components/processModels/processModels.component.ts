import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { ProcessesService } from '../../../../Processes.service';

@Component({
  selector: 'models',
  styles: [],
  template: require('./processModels.html')
})
export class ProcessModels implements OnInit {
   processModels = [];
   error = undefined;

  constructor(protected service:ProcessesService) {}

  ngOnInit(): void {
    var that = this;
    this.service.getProcessModels()
      .subscribe(
         data => {
            console.log(data);
            that.processModels = JSON.parse(data['_body']);
         },
         err => that.error = err,
         () => console.log('Request Complete')
       );
  }

  deleteProcessModel(pmId:number):void {
      console.log("delete processModel: " +pmId);
  }
}
