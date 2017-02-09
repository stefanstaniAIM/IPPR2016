import {Component, ViewEncapsulation} from '@angular/core';
import {BaThemeConfigProvider} from '../../../theme';
import { ProcessesService } from '../../../Processes.service';
import { User } from '../../../user';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'todo',
  encapsulation: ViewEncapsulation.None,
  styles: [require('./todo.scss')],
  template: require('./todo.html')
})
export class Todo {

  public dashboardColors = this._baConfig.get().colors.dashboard;

  public tasks:[
    {
      piId:number,
      processName:string,
      ssId:number,
      stateName:string,
      functionType:string,
      lastChanged:number[]
    }
  ];

  constructor(private _baConfig:BaThemeConfigProvider, private _processService:ProcessesService, protected user:User, protected route: ActivatedRoute, protected router: Router) {
    var that = this;
    this._processService.getProcessTasksForUser(this.user.getUid())
    .subscribe(
        data => {
          that.tasks = JSON.parse(data['_body']);
        },
        err =>{
          console.log(err);
        }
      );
  }

  goToProcess(id){
    this.router.navigate(['../../myprocesses/active', id], { relativeTo: this.route });
  }
}
