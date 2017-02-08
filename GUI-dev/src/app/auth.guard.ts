import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable()
export class AuthGuard implements CanActivate {

    constructor(private _router: Router, private _authService:AuthService) { }

    canActivate() {
        if (this._authService.isLoggedIn()) {
            return true;
        } else {
           // not logged in so redirect to login page
           this._router.navigate(['/'])
           return false;
        }
    }
}
