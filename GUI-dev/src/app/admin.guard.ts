import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { User } from './user';

@Injectable()
export class AdminGuard implements CanActivate {

    constructor(private _router: Router, private _user:User) { }

    canActivate() {
        if (this._user.isAdmin()) {
            return true;
        } else {
           // not admin so redirect ot dashboard
           this._router.navigate(['/'])
           return false;
        }
    }
}
