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
   currentSelectedField;
   currentBofms;
   buildedBofps = {};

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
    var processModelResult;
    this.getFormData(this.currentSelectedBusinessObject);
    processModelResult = this.processModel;
    processModelResult.bofms = this.getBofms();
    processModelResult.bofps = [];
    Object.keys(this.buildedBofps).forEach(a => processModelResult.bofps = processModelResult.bofps.concat(Object.values(this.buildedBofps[a])));
  }

  getBofms(){
    var result = [];
    if(this.currentSelectedBusinessObject){
      //buildedBusinessObjects should not be empty...
      for(var bom in this.buildedBusinessObjects) {
        if(Object.keys(this.buildedBusinessObjects[bom]).length > 0) {
          var values = JSON.parse(this.buildedBusinessObjects[bom]);
          values.forEach(value => {
            result.push({
              name: value.label,
              type: value.type,
              bomId: bom,
              id: value.name
            })
          });
        }
      }
    }
    return result;
  }

  initFormBuilder(businessObject): void {
    var options = {
      dataType: 'json', // default: 'xml',
      typeUserAttrs: {
        "radio-group": {
          permissions: {
            label: 'Permissions'
          },
          mandatory: {
            label: 'Mandatory'
          }
        },
        text: {
          className: {
            label: 'Class',
            options: {
              'red form-control': 'Red',
              'green form-control': 'Green',
              'blue form-control': 'Blue'
            },
            style: 'border: 1px solid red'
          }
        }
      },
      disableFields: ['autocomplete', 'button', 'checkbox-group', 'file', 'header', 'hidden', 'paragraph', 'select', 'textarea'],
      showActionButtons: false
    };
    this.formBuilder = jQuery(".formBuilder").formBuilder(options).data('formBuilder');
  }

  getFormData(businessObject): void {
    var that = this;
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
    this.currentBofms = this.getBofms().filter(b => b.bomId === this.currentSelectedBusinessObject.id);
    //Add new fields
    var allBofms = this.getBofms();
    allBofms.forEach(field => {
      var boms = that.processModel.boms.filter(bom => bom.id === field.bomId);
      if(!that.buildedBofps[field.id]) {
        that.buildedBofps[field.id] = {};
        boms.forEach(bom => {
          bom.stateIds.forEach(stateId => {
            that.buildedBofps[field.id][stateId] = {
              read: true,
              write: true,
              mandatory: false,
              stateId: stateId,
              bofmId: field.id
            }
          })
        })
      }
    });

    //remove deleted fields
    Object.keys(this.buildedBofps).forEach(fieldId => {
      if(allBofms.filter(b => b.id === fieldId).length < 1) {
          delete this.buildedBofps[fieldId];
      }
    });
  }

  getStateName(stateId): string {
    return this.processModel.states.filter(s => s.id === stateId)[0].name;
  }

  getSubjectModelName(stateId): string {
    var subjectModelId = this.processModel.states.filter(s => s.id === stateId)[0].subjectModelId;
    return this.processModel.subjectModels.filter(sm => sm.id === subjectModelId)[0].name;
  }
}
