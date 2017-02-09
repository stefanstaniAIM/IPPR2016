import { Component,  OnInit } from '@angular/core';
import { ProcessesService } from '../../../../Processes.service';
import { BaThemeSpinner } from '../../../../theme/services';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'activeProcesses',
  styles: [],
  template:  require('./activeProcesses.html')
})
export class ActiveProcesses implements OnInit  {

  activeProcesses:[
    {
      piId:number,
      startTime:number[],
      processName:string,
      startUserId:number
    }
  ];
  msg = undefined;

  constructor(protected service: ProcessesService, protected spinner:BaThemeSpinner, protected route: ActivatedRoute, protected router: Router) {
  }

  ngOnInit() {
    var that = this;
    this.spinner.show();
    this.service.getProcessTasksForUser()
    .subscribe(
        data => {
          that.activeProcesses = JSON.parse(data['_body']);
          that.spinner.hide();
        },
        err =>{
          that.msg = {text: err, type: 'error'}
          console.log(err);
          that.spinner.hide();
        }
      );
  }

  showProcess(piId:number){
    this.router.navigate(['../active', piId], { relativeTo: this.route });
  }
}
