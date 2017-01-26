import {Component, Input} from '@angular/core';

@Component ({
  selector: 'business-objects',
  template: `
  <div *ngFor="let businessObject of businessObjects">
    <ba-card title="{{businessObject.name}}" baCardClass="with-scroll">
    <div *ngFor="let field of businessObject.fields">
      {{field.name}}
      <div class="form-group">
        <div [ngSwitch]="field.type">
          <label class="control-label" *ngIf="field.type !== 'checkbox'">{{field.description}}</label>
          <input type="text" *ngSwitchCase="'STRING'" class="form-control" id="{{field.id}}" name="{{businessObject.bomId}}-:_{{field.bofmId}}" [ngModel]="field.value" [attr.maxlength]="field.maxlength" [attr.minlength]="field.minlength" [attr.required]="field.required" [readonly]="field.readonly">
          <input type="number" *ngSwitchCase="'number'" class="form-control" id="{{field.id}}" name="{{businessObject.bomId}}-:_{{field.bofmId}}" [ngModel]="field.value" [attr.max]="field.max" [attr.min]="field.min" [attr.required]="field.required" [readonly]="field.readonly">
          <ba-checkbox *ngSwitchCase="'checkbox'" name="{{businessObject.bomId}}-:_{{field.readonly ? field.name + '-:_control' : field.bofmId }}" [ngModel]="field.value" [value]="field.value" [label]="field.description" [onChangeCheckboxFn]="onChangeCheckboxFn" [disabled]="field.readonly"></ba-checkbox>
          <div class="row" *ngSwitchCase="'radio'">
            <div class="col-sm-1" *ngFor="let choice of field.choices">
              <label class="radio-inline nowrap">
              <input type="radio" [disabled]="field.readonly" [ngModelOptions]="{standalone: true}" [(ngModel)]="field.value" [value]="choice.name" [checked]="choice.name === field.value"/>
              <span>{{choice.description}}</span>
            </label>
          </div>
        </div>
        <input type="checkbox" *ngIf="field.type === 'checkbox' && field.readonly" name="{{field.name}}" [ngModel]="field.value" value="{{field.value}}" style="display:none" /> <!-- Dirty Hack for sending checkbox value if checkbox is readonly (angular does not send hidden fields) -->
        <input type="radio" *ngIf="field.type === 'radio'" name="{{businessObject.bomId}}-:_{{field.bofmId}}" [ngModel]="field.value" value="{{field.value}}" style="display:none" /> <!-- Dirty Hack for sending radiofield value -->
        </div>
      </div>
    </div>
  </ba-card>
  <business-objects [businessObjects]=businessObject.children></business-objects>
</div>
  `
})
export class BusinessObjects {
   @Input() businessObjects: any[];
}
