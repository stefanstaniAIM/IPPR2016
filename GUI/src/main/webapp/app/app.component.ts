import { Component } from '@angular/core';
@Component({
  selector: 'app',
  template: `
    <navbar></navbar>
    <h1>{{title}}</h1>
    <a routerLink="/dashboard">Dashboard</a>
    <router-outlet></router-outlet>
  `
})
export class AppComponent {
  title = 'IPPR';
}