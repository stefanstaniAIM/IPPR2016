import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { ProcessesService } from '../../../../Processes.service';
import './importModelFormBuilder.loader.ts';

@Component({
  selector: 'import',
  styles: [require('./importProcessModel.scss')],
  template: require('./importProcessModel.html')
})
export class ImportProcessModel implements OnInit {
   processModel;
   rules;
   error = undefined;
   formBuilder;
   buildedBusinessObjects = {};
   currentSelectedBusinessObject;

  constructor(protected service:ProcessesService) {}

  ngOnInit(): void {
   /*this.service.getProcessModels()
      .subscribe(
         data => {
            console.log(data);
            this.processModels = JSON.parse(data['_body']);
         },
         err => this.error = err,
         () => console.log('Request Complete')
       );*/
  }

  uploadOWLModel(form):void {
    var that = this;
    console.log("upload owl model");
    this.service.uploadOWLModel(null)
       .subscribe(
          data => {
             console.log(data);
             that.processModel = JSON.parse(data['_body']);
             that.processModel.boms.forEach(businessObject => {
               that.buildedBusinessObjects[businessObject.id] = {};
             });
             that.initRules();
          },
          err => this.error = err,
          () => {
            console.log('Request Complete');
          }
        );
  }

  initRules():void{
    var that = this;
    this.service.getRules()
       .subscribe(
          data => {
             console.log(data);
             that.rules = JSON.parse(data['_body']);
             that.currentSelectedBusinessObject = that.processModel.boms[0];
             that.initFormBuilder(that.currentSelectedBusinessObject);
          },
          err => this.error = err,
          () => console.log('Request Complete')
        );
  }

  uploadProcessModel(form):void {
    var that = this;
    this.getFormData(this.currentSelectedBusinessObject);
    var processModelResult;
    processModelResult = this.processModel;
    processModelResult.bofms = [];
    processModelResult.bofps = [];
    var bofmId = 0;
    //buildedBusinessObjects should not be empty...
    for(var bom in this.buildedBusinessObjects) {
      if(Object.keys(this.buildedBusinessObjects[bom]).length > 0) {
        var values = JSON.parse(this.buildedBusinessObjects[bom]);
        values.forEach(value => {
          bofmValues = []; //not sure how to really find them
          bofmValues.forEach(bofmValue => {
            processModelResult.bofps.push({
              bofmId: bofmId,
              stateId: bofmValue.stateId,
              permission: bofmValue.permission,
              mandatory: bofmValue.mandatory
            });
          });
          processModelResult.bofms.push({
            name: value.label,
            type: value.type,
            bom: bom,
            id: bofmId++
          })
        });
        console.log(this.buildedBusinessObjects[bom])
      }
    }
    console.log(processModelResult);
  }

  initFormBuilder(businessObject): void {
    var options = {
      dataType: 'json', // default: 'xml',
      disableFields: ['autocomplete', 'button', 'checkbox-group', 'file', 'header', 'hidden', 'paragraph', 'select', 'text'],
      showActionButtons: false
    };
    this.formBuilder = jQuery(".formBuilder").formBuilder(options).data('formBuilder');
  }

  getFormData(businessObject): void {
    if(this.currentSelectedBusinessObject !== businessObject){
      this.buildedBusinessObjects[this.currentSelectedBusinessObject.id] = this.formBuilder.formData;
      var formData = this.buildedBusinessObjects[businessObject.id];
      formData = jQuery.isEmptyObject(formData) ? undefined : formData === "[]" ? undefined : formData;
      if(formData !== undefined){
        //This is a necessary hack, otherwise setData will not work correctly
        this.formBuilder.actions.addField(
        	{
        		"type": "paragraph",
        		"subtype": "p",
        		"label": "Paragraph",
        		"className": "paragraph"
        	}
        );
        this.formBuilder.actions.setData(formData);
      } else {
        this.formBuilder.actions.clearFields();
      }
    } else {
      this.buildedBusinessObjects[businessObject.id] = this.formBuilder.formData;
    }
    this.currentSelectedBusinessObject = businessObject;
    console.log(this.formBuilder.formData);
  }
}
