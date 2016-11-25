import { NgModule } from '@angular/core';
import { CommonModule } from "@angular/common";

import { CommaDatePipe } from "./CommaDate.pipe";

@NgModule({
  declarations:[CommaDatePipe],
  imports:[ CommonModule ],
  exports:[ CommaDatePipe ]
})

export class Pipes{}
