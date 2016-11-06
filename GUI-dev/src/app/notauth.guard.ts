import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { AuthService } from './auth.service';
 
@Injectable()
export class NotAuthGuard implements CanActivate {
 
    constructor(private _router: Router, private _authService:AuthService) { }
 
    canActivate() {
        if (this._authService.isLoggedIn()) {
            // logged in so redirect to dashboard
            this._router.navigate(['Dashboard'])
            return false;
        } else {
            return true;
        }
    }
}