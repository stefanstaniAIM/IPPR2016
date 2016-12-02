import {Component, OnChanges, Input, trigger, state, style, transition, animate} from '@angular/core';

@Component({
  selector : 'sm-nav-element',
  animations: [
    trigger('accMenuSlideInOut', [
      state('true', style({
        opacity: 1,
        transform: 'scale(1.0)'
      })),
      state('false', style({
        height: 0,
        padding: 0,
        opacity: 0,
        transform: 'scale(0.0)'
      })),
      transition('1 => 0', animate('200ms ease-in-out')),
      transition('0 => 1', animate('200ms ease-in-out'))
    ])
  ],
  templateUrl: 'sm-nav-element.component.html',
  styleUrls: ['sm-nav-element.component.scss']
})
export class SmNavElementComponent {
  @Input() public heading:string;

  isVisible : boolean = true;
}
