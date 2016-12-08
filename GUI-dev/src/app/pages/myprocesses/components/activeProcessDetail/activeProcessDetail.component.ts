import 'rxjs/add/operator/switchMap';
import { Component,  OnInit } from '@angular/core';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { ProcessesService } from '../../Processes.service';

@Component({
  selector: 'activeProcessDetail',
  styles: [],
  template:  require('./activeProcessDetail.html')
})
export class ActiveProcessDetail implements OnInit {

  piId:number;
  msg = undefined;
  subjectsState:{
    piId:number,
    status: string,
    subjects: [{
      lastChanged: number[],
      receiveState: string,
      ssId: number,
      stateFunctionType: string,
      stateName: string,
      subjectName: string,
      userId: number
    }]
  };

  constructor(protected service: ProcessesService, protected route: ActivatedRoute, protected router: Router) {
  }

  ngOnInit() {
  this.piId = +this.route.snapshot.params['piId'];
  //this.route.params
    //.switchMap((params: Params) => service.loadprocess etc. +params['piId'])
    //.subscribe((piId:number) => this.piId = piId)
    this.service.getProcessSubjectsState(this.piId)
    .subscribe(
        data => {
          this.subjectsState = JSON.parse(data['_body']);
        },
        err =>{
          this.msg = {text: err, type: 'error'}
          console.log(err);
        },
        () => console.log("Request done")
      );
}
}
