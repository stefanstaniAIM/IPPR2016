import { Injectable } from '@angular/core';
import { AuthHttp } from 'angular2-jwt';
import { User } from './user';

@Injectable()
export class EventLoggerService {

  restApi = window.location.protocol + "//" + window.location.hostname + ":10000/ev";

  constructor(private _authHttp:AuthHttp, private _user:User){}

   getEventLog(pmId){
    return this._authHttp.get(this.restApi+'/eventlog/'+pmId);
   }

   getEventLogDownloadLink(pmId){
     return this.restApi+'/eventlogCSV/'+pmId;
   }

   uploadOWLModel(owlModel){
     return this._authHttp.post(this.restApi+'/owlprocessmodel/', owlModel);
   }
}
