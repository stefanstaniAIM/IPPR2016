import {Injectable} from "@angular/core";

@Injectable()
export class User {
   private firstname: string;
   private lastname: string;
   private username: string;
   private rules: any[];
   private roles: any[];
   private uid: number;

   constructor() {
   }

   public set(firstname:string, lastname:string, username:string, rules:string[], roles:string[], uid:number) {
      this.firstname = firstname;
      this.lastname = lastname;
      this.username = username;
      this.rules = rules;
      this.roles = roles;
      this.uid = uid;
   }

   public getUid() {
      return this.uid;
   }

   public isAdmin() {
      return this.rules.filter(function(rule){return rule.name.toLowerCase() === "admin_rule"}).length ? true : false;
   }

}
