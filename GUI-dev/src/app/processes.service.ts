import { Injectable } from '@angular/core';
import { AuthHttp } from 'angular2-jwt';
import { User } from './user';

@Injectable()
export class ProcessesService {

  constructor(private _authHttp:AuthHttp, private _user:User){}

   getProcessModels(){
    return this._authHttp.get('http://localhost:10000/api/processes/toStart?page=0');
   }

   startProcess(pmId: number){
     return this._authHttp.post('http://localhost:10000/api/processes/startProcess', {
      "pmId": pmId,
      "startUserId": this._user.getUid()
      });
   }

   getAmountOfActiveProcesses(){
     return this._authHttp.get('http://localhost:10000/api/processes/amountOfActiveProcesses')
   }

   getAmountOfActiveProcessesForUser(userId:number){
     return this._authHttp.get('http://localhost:10000/api/processes/amountOfActiveProcessesPerUser/'+userId);
   }

   getPossibleUsersForProcessModel(groupName:string){
      return this._authHttp.get('http://localhost:10000/api/processes/possibleUsers/'+groupName);
   }

   getProcessState(piId:number){
       return this._authHttp.get('http://localhost:10000/api/processes/state/'+piId);
   }

   getActiveProcesses(){
      return this._authHttp.get('http://localhost:10000/api/processes/active?page=0');
   }

   getActiveProcessesForUser(){
     return this._authHttp.get('http://localhost:10000/api/processes/active/'+this._user.getUid()+'?page=0');
   }

   getUserById(userId:number){
      return this._authHttp.get('http://localhost:10000/api/user/'+userId);
   }

   stopProcess(piId:number){
      return this._authHttp.get('http://localhost:10000/api/processes/stop/'+piId);
   }
}
