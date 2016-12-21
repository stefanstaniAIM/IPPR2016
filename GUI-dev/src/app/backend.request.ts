import { Inject, Injectable } from '@angular/core';
import { Http, Headers } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { tokenNotExpired } from 'angular2-jwt';

@Injectable()
export class BackendRequestClass {

    private result: Object = null;

    constructor(private http: Http) {

    }

    public getResult() {
        return this.result;
    }

    public load() {

        let headers = new Headers({ 'Content-Type': 'application/json' });
        headers.append('Authorization', 'Bearer '+ localStorage.getItem("token"));
        if(tokenNotExpired("token")){
            return new Promise((resolve, reject) => {
               this.http.get(window.location.protocol + "//" + window.location.hostname + ":10000/api/me/", {headers:headers}).map( res => res.json() ).catch((error: any):any => {
                   reject(false);
                   return Observable.throw(error.json().error || 'Server error');
               }).subscribe( (callResult) => {
                   this.result = callResult;
                   resolve(true);
               });
           });
        } else {
        return Promise.resolve("not logged in");}
    }
}
