import { Injectable } from '@angular/core';
import { AuthHttp } from 'angular2-jwt';

@Injectable()
export class StartableProcessesService {

  constructor(private _authHttp:AuthHttp){}

   getProcessModels(){
    return this._authHttp.get('http://localhost:10000/api/processesToStart?page=0');
   }

   startProcess(pmId: number){
     return this._authHttp.post('http://localhost:10000/api/startProcess', {
      "pmId": pmId,
      "assignments": [
        {
          "smId": 2,
          "userId": 1
        },
        {
          "smId": 1,
          "userId": 2
        }
       ]
      });
   }

}
