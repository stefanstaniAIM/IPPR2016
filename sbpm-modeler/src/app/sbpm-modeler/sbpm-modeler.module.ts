import { NgModule } from '@angular/core';
import { Ng2BootstrapModule } from 'ng2-bootstrap';

import { SbpmModelerComponent } from './sbpm-modeler.component';
import { ROUTING } from './sbpm-modeler.routes';
import { SmNavElementComponent } from './sm-nav/sm-nav-element.component'

@NgModule({
    imports: [
        Ng2BootstrapModule,
        ROUTING
    ],
    declarations: [SbpmModelerComponent, SmNavElementComponent]
})
export class SbpmModelerModule { }
