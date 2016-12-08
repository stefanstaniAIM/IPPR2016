import {Injectable} from "@angular/core";

@Injectable()
export class User {
   private firstname: string;
   private lastname: string;
   private username: string;
   private groups: any[];
   private uid: number;

   constructor() {
   }

   public set(firstname:string, lastname:string, username:string, groups:string[], uid:number) {
      this.firstname = firstname;
      this.lastname = lastname;
      this.username = username;
      this.groups = groups;
      this.uid = uid;
   }

   public getUid() {
      return this.uid;
   }

   public isAdmin() {

      //return true;
      return this.groups.filter(function(group){return group.name.toLowerCase() === "admin"}).length ? true : false;
   }

}
