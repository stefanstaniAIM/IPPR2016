import {Component, ViewEncapsulation} from '@angular/core';
import { AuthHttp } from 'angular2-jwt';
import { OnInit } from '@angular/core';

@Component({
  selector: 'processModels',
  encapsulation: ViewEncapsulation.None,
  styles: [require('./processModels.scss')],
  template: require('./processModels.html')
})
export class ProcessModels implements OnInit {

   processModels = [];

  constructor( private _authHttp:AuthHttp) {
  }
  
  ngOnInit(): void {
    this._authHttp.get('http://localhost:10000/processes?page=0').subscribe(
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
