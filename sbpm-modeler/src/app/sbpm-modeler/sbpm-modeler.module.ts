import { NgModule } from '@angular/core';

import { SbpmModelerComponent }   from './sbpm-modeler.component';
import { ROUTING } from './sbpm-modeler.routes';

@NgModule({
    imports: [ROUTING],
    declarations: [SbpmModelerComponent]
})
export class SbpmModelerModule { }