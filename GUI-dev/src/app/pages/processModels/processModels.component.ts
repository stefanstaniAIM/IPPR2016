import {Component, ViewEncapsulation} from '@angular/core';
import { OnInit } from '@angular/core';
import { ProcessModelsService } from './processModels.service';

@Component({
  selector: 'processModels',
  encapsulation: ViewEncapsulation.None,
  styles: [require('./processModels.scss')],
  template: require('./processModels.html')
})
export class ProcessModels implements OnInit {

   processModels = [];

  constructor(protected service:ProcessModelsService) {
  }
  
  ngOnInit(): void {
   this.service.getProcessModels()
      .subscribe(
         data => {
            console.log(data);
            this.processModels = JSON.parse(data['_body']);
         },
         err => console.log('ERROR: '+err),
         () => console.log('Request Complete')
       );
  }

  newProcess(pmId:number):void {
      console.log("start new process: " +pmId);
  }
  
}
