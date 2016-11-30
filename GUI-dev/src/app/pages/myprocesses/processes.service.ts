import { Injectable } from '@angular/core';
import { AuthHttp } from 'angular2-jwt';

@Injectable()
export class ProcessesService {

  constructor(private _authHttp:AuthHttp){}

   getProcessModels(){
    return this._authHttp.get('http://localhost:10000/api/processes/toStart?page=0');
   }

   startProcess(pmId: number, subjectModels = []){
     return this._authHttp.post('http://localhost:10000/api/processes/startProcess', {
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

   getAmountOfActiveProcesses(){
     return this._authHttp.get('http://localhost:10000/api/processes/amountOfActiveProcesses')
   }

   getAmountOfActiveProcessesPerUser(userId:number){
     return this._authHttp.get('http://localhost:10000/api/processes/amountOfActiveProcessesPerUser/'+userId);
   }

   getPossibleUsersForProcessModel(groupName:string){
      return this._authHttp.get('http://localhost:10000/api/processes/possibleUsers/'+groupName);
   }
}
