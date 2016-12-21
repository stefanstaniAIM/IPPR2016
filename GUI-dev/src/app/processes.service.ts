import { Injectable } from '@angular/core';
import { AuthHttp } from 'angular2-jwt';
import { User } from './user';

@Injectable()
export class ProcessesService {

  restApi = window.location.protocol + "//" + window.location.hostname + ":10000/api";

  constructor(private _authHttp:AuthHttp, private _user:User){}

   getProcessModels(){
    return this._authHttp.get(this.restApi+'/processes/toStart?page=0');
   }

   startProcess(pmId: number){
     return this._authHttp.post(this.restApi+'/processes/startProcess', {
      "pmId": pmId,
      "startUserId": this._user.getUid()
      });
   }

   getAmountOfActiveProcesses(){
     return this._authHttp.get(this.restApi+'/processes/amountOfActiveProcesses')
   }

   getAmountOfActiveProcessesForUser(userId:number){
     return this._authHttp.get(this.restApi+'/processes/amountOfActiveProcessesPerUser/'+userId);
   }

   getPossibleUsersForProcessModel(groupName:string){
      return this._authHttp.get(this.restApi+'/processes/possibleUsers/'+groupName);
   }

   getProcessState(piId:number){
       return this._authHttp.get(this.restApi+'/processes/state/'+piId);
   }

   getActiveProcesses(){
      return this._authHttp.get(this.restApi+'/processes/active?page=0');
   }

   getActiveProcessesForUser(){
     return this._authHttp.get(this.restApi+'/processes/active/'+this._user.getUid()+'?page=0');
   }

   getUserById(userId:number){
      return this._authHttp.get(this.restApi+'/user/'+userId);
   }

   stopProcess(piId:number){
      return this._authHttp.post(this.restApi+'/processes/stop/'+piId, {});
   }

   getProcessTasksForUser(userId:number){
     return this._authHttp.get(this.restApi+'/processes/tasks/'+userId);
   }

   getTasksForProcessForUser(piId:number){
     return this._authHttp.get(this.restApi+'/processes/task/'+piId+'/'+this._user.getUid());
   }
}
