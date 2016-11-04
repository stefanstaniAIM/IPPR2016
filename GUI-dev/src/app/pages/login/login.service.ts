import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import { Headers, RequestOptions, Http, Jsonp } from '@angular/http';
 
@Injectable()
export class LoginService {
 
  constructor(
    private _router: Router,
    private _http: Http){}
 
  logout() {
    localStorage.removeItem("id_token");
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
         data => this.saveJwt(data.id_token),
         err => false,
         () => this._router.navigate(['Dashboard'])
       ); 
  }
  
   saveJwt(jwt) {
     if(jwt) {
       localStorage.setItem('id_token', jwt)
     }
   }
 
   checkCredentials(){
    if (localStorage.getItem("id_token") === null){
        this._router.navigate(['Login']);
    }
  } 
}