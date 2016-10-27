import { Component } from '@angular/core';
@Component({
  selector: 'dashboard',
  template: '<h2>{{title}}</h2>'
})
export class DashboardComponent {
    title = "Dashboard"
}