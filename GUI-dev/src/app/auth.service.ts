import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Headers, RequestOptions, Http, Jsonp } from '@angular/http';
import { AuthHttp, tokenNotExpired } from 'angular2-jwt';
import { User } from './user';
import { BackendRequestClass } from './backend.request';

@Injectable()
export class AuthService {

  constructor(
    private _router: Router,
    private _http: Http,
    private _authHttp:AuthHttp,
    private _user:User,
    private _backendRequest: BackendRequestClass){
    }

  logout() {
    localStorage.removeItem("token");
    this._router.navigate(['/']);
  }

  login(username, password){
    var that = this;
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    this._http.post(window.location.protocol + "//" + window.location.hostname + ":10000/user/login/", {'username':username, 'password':password}, {
       headers: headers
     }).map(res => res.json())
       .subscribe(
         data => that.saveJwt(data.token),
         err => console.error("Login failed: "+err)
       );
  }

  saveJwt(jwt) {
    var that = this;
    if(jwt) {
      localStorage.setItem('token', jwt);
      this._backendRequest.load().then(function(){
        that.initUser();
      });
    }
  }

   checkLogin(){
    if(!this.isLoggedIn()){
       this._router.navigate(['Login']);
    }
   }

   isLoggedIn(){
      return tokenNotExpired('token');
   }

   initUser() {
      const result = <any> this._backendRequest.getResult();
      this._user.set(result.firstname, result.lastname, result.username, result.rules, result.roles, result.uid);
      this._router.navigate(['Dashboard'])

   }
}
