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
   this.service.getProcessModels()
      .subscribe(
         data => {
            console.log(data);
            this.processModels = JSON.parse(data['_body']);
         },
         err => this.error = err,
         () => console.log('Request Complete')
       );
  }

  deleteProcessModel(pmId:number):void {
      console.log("delete processModel: " +pmId);
  }
}
