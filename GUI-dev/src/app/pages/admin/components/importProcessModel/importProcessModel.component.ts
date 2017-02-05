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
   buildedBusinessObjects = [];
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
               that.buildedBusinessObjects[businessObject.name] = {};
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
    console.log(form);
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
      this.buildedBusinessObjects[this.currentSelectedBusinessObject.name] = this.formBuilder.formData;
      var formData = this.buildedBusinessObjects[businessObject.name];
      formData = jQuery.isEmptyObject(formData) ? undefined : formData === "[]" ? undefined : formData;
      if(formData !== undefined){
        //This is a hack, otherwise setData will not work correctly
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
    }
    this.currentSelectedBusinessObject = businessObject;
    console.log(this.formBuilder.formData);
  }
}
