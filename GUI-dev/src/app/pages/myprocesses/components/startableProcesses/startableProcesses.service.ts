import { Injectable } from '@angular/core';
import { AuthHttp } from 'angular2-jwt';

@Injectable()
export class StartableProcessesService {

  constructor(private _authHttp:AuthHttp){}

   getProcessModels(){
    return this._authHttp.get('http://localhost:10000/api/processesToStart?page=0');
   }

}
