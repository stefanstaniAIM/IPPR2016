import { Component } from '@angular/core';

@Component({
  selector: 'sbpm-modeler',
  templateUrl: './sbpm-modeler.component.html',
  styles: ['./sbpm-modeler.component.scss']
})
export class SbpmModelerComponent {
  
  constructor() {

  }

  ngOnInit() {
    console.log('hello `sbpm-modeler` component');
  }
}
