import { NgModule } from '@angular/core';
import { Ng2BootstrapModule } from 'ng2-bootstrap';

import { SbpmModelerComponent } from './sbpm-modeler.component';
import { ROUTING } from './sbpm-modeler.routes';

@NgModule({
    imports: [
        Ng2BootstrapModule,
        ROUTING
    ],
    declarations: [SbpmModelerComponent]
})
export class SbpmModelerModule { }