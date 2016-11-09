import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Headers, RequestOptions, Http, Jsonp } from '@angular/http';
import { AuthHttp, tokenNotExpired } from 'angular2-jwt';
 
@Injectable()
export class AuthService {
   
   private roles: string[];
 
  constructor(
    private _router: Router,
    private _http: Http,
    private _authHttp:AuthHttp){
    }
 
  logout() {
    localStorage.removeItem("token");
    this._router.navigate(['Login']);
  }
 
  login(username, password){
     var headers = new Headers();
     headers.append('Content-Type', 'application/json');

     this._http.post('http://localhost:10000/user/login/', {'name':username, 'password':password}, {
       headers: headers
       })
       .map(res => res.json())
       .subscribe(
         data => this.saveJwt(data.token),
         err => console.error("Login failed: "+err),
         () => this._router.navigate(['Dashboard'])
       ); 
  }
  
   saveJwt(jwt) {
     if(jwt) {
         localStorage.setItem('token', jwt);
         this.setRoles();
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
   
   getUserData() {
       return this._authHttp.get('http://localhost:10000/api/roles/').map(res => res.json());
   }
   
   setRoles() {
      this.getUserData().subscribe(
        data => {
         this.roles = data;
         console.log(this.roles);
        },
        err => this.logout()
      );
   }
   
   getRoles() {
      return this.roles;
   }
}