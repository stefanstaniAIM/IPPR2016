import { Injectable } from '@angular/core';
import { AuthHttp } from 'angular2-jwt';
import { User } from './user';

@Injectable()
export class EventLoggerService {

  restApi = window.location.protocol + "//" + window.location.hostname + ":10000/ev";

  constructor(private _authHttp:AuthHttp, private _user:User){}

   getEventLog(pmId, subject){
    return this._authHttp.get(this.restApi+'/eventlog/'+pmId+'/'+subject);
   }

   getEventLogDownloadLink(pmId, subject){
     return this.restApi+'/eventlogCSV/'+pmId+'/'+subject;
   }

   manipulatePNML(pnmlFile, csvFile){
     let data = {
      "pnmlContent": pnmlFile,
      "csvLog": csvFile
    };
    let body = JSON.stringify(data);
     return this._authHttp.post(this.restApi+'/manipulatePNML/', body);
   }

   generateOWL(processModelName, pnmlFiles){
     return this._authHttp.post(this.restApi+'/generateOWL/', {processModelName, pnmlFiles});
   }
}
