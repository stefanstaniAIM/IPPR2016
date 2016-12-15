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
    this.spinner.show();
    this.service.getActiveProcesses()
    .subscribe(
        data => {
          this.activeProcesses = JSON.parse(data['_body']);
          this.spinner.hide();
        },
        err =>{
          this.msg = {text: err, type: 'error'}
          console.log(err);
          this.spinner.hide();
        }
      );
  }

  showProcess(piId:number){
    this.router.navigate(['../active', piId], { relativeTo: this.route });
  }

  stopProcess(piId:number){
    this.spinner.show();
    this.service.stopProcess(piId)
    .subscribe(
        data => {
          console.log(data);
          this.spinner.hide();
        },
        err =>{
          this.msg = {text: err, type: 'error'}
          console.log(err);
          this.spinner.hide();
        }
      );
  }
}
