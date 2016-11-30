import { Component, trigger, state, style, transition, animate } from '@angular/core';

@Component({
  selector: 'sbpm-modeler',
  templateUrl: './sbpm-modeler.component.html',
  styleUrls: ['./sbpm-modeler.component.scss'],
  animations: [
    trigger('sideNavOpenedSlideInOut', [
      state('in', style({
        transform: 'translate3d(-15vw, 0, 0)'
      })),
      state('out', style({
        transform: 'translate3d(0, 0, 0)'
      })),
      transition('in => out', animate('1000ms ease-in-out')),
      transition('out => in', animate('1000ms ease-in-out'))
    ]),
    trigger('sideNavCollapsedSlideInOut', [
      state('in', style({
        transform: 'translate3d(-5vw, 0, 0)'
      })),
      state('out', style({
        transform: 'translate3d(0, 0, 0)'
      })),
      transition('in => out', animate('1000ms ease-in-out')),
      transition('out => in', animate('1000ms ease-in-out'))
    ]),
  ]
})
export class SbpmModelerComponent {

  constructor() {

  }

  ngOnInit() {
    console.log('hello `sbpm-modeler` component');
  }

  sideNavOpenedState:string = 'out';
  sideNavCollapsedState:string = 'in';

  toggleMenu() {
    // 1-line if statement that toggles the value:
    this.sideNavOpenedState = this.sideNavOpenedState === 'out' ? 'in' : 'out';
    this.sideNavCollapsedState = this.sideNavCollapsedState === 'in' ? 'out' : 'in';
  }
}
