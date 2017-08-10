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
   currentSelectedFieldId;
   currentBofms;
   buildedBofps = {};
   success;
   owlFile;
   version = 5;

  constructor(protected service:ProcessesService) {}

  ngOnInit(): void {
  }

  onFileChange(event) {
    var that = this;
    this.owlFile = event.srcElement.files[0];
    var split = this.owlFile.name.split(".");
    if(split[split.length-1] !== "owl") {
      this.owlFile = undefined;
      event.target.value = "";
    }
  }

  uploadOWLModel(form):void {
    var that = this;
    var reader = new FileReader();
    if(this.owlFile) {
      reader.onload = function(e) {
        var body = {owlContent: reader.result, version: "0.7."+that.version}
        that.service.uploadOWLModel(body)
        .subscribe(
            data => {
               that.processModel = JSON.parse(data['_body']);
               that.processModel.boms.forEach(businessObject => {
                 that.buildedBusinessObjects[businessObject.id] = {};
               });
               that.initRules();
            },
            err => that.error = "Die OWL Datei konnte nicht richtig interpretiert werden!",
            () => {
            }
          );
      }
      reader.readAsText(this.owlFile);
    }
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
          err => that.error = err,
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
    Object.keys(this.buildedBofps).forEach(a => processModelResult.bofps = processModelResult.bofps.concat((<any>Object).values(this.buildedBofps[a])));
    this.service.importProcessModel(processModelResult)
       .subscribe(
          data => {
             if(JSON.parse(data['_body']) === true){
               that.processModel = undefined;
               that.rules= undefined;
               that.error = undefined;
               that.formBuilder= undefined;
               that.buildedBusinessObjects = {};
               that.currentSelectedBusinessObject= undefined;
               that.currentSelectedFieldId= undefined;
               that.currentBofms= undefined;
               that.buildedBofps = {};
               that.success = "Das Prozessmodell wurde erfolgreich importiert!";
             } else {
               that.error = "Das Prozessmodell konnte nicht importiert werden!";
               window.scrollTo(0, 0);
             }
          },
          err => that.error = "Das Prozessmodell konnte nicht importiert werden!",
          () => console.log('Request Complete')
        );
  }

  getBofms(){
    var result = [];
    if(this.currentSelectedBusinessObject){
      for(var bom in this.buildedBusinessObjects) {
        if(Object.keys(this.buildedBusinessObjects[bom]).length > 0) {
          var values = JSON.parse(this.buildedBusinessObjects[bom]);
          values.forEach(value => {
            if(value.type !== "paragraph"){
              var type;
              switch (value.type) {
                case "text":
                  type = "STRING";
                  break;
                case "number":
                  type = "NUMBER";
                  break;
                case "date":
                  type = "DATE";
                  break;
                case "checkbox":
                  type = "CHECKBOX";
                  break;
                default:
                  type = "STRING";
                  break;
              }
              result.push({
                name: value.label,
                type: type,
                bomId: bom,
                id: value.name
              })
            }
          });
        }
      }
    }
    return result;
  }

  initFormBuilder(businessObject): void {
    var that = this;
    var options = {
      dataType: 'json', // default: 'xml',
      disableFields: ['autocomplete', 'button', 'checkbox-group', 'file', 'header', 'hidden', 'paragraph', 'select', 'textarea'],
      showActionButtons: false
    };
    this.formBuilder = jQuery(".formBuilder").formBuilder(options).data('formBuilder');

    /*//Timeout, otherwise the formData will still be the old value
    document.addEventListener("fieldAdded", function(e){
      setTimeout(function(){
        that.getFormData(that.currentSelectedBusinessObject, true);
      }, 250);
    });
    document.addEventListener("fieldRemoved", function(e){
      setTimeout(function(){
        that.getFormData(that.currentSelectedBusinessObject, true);
      }, 250);
    });*/
  }

  getFormData(businessObject, internal?:boolean): void {
    var that = this;
    if(this.currentSelectedBusinessObject !== businessObject){
      this.buildedBusinessObjects[this.currentSelectedBusinessObject.id] = this.formBuilder.formData;
      var formData = this.buildedBusinessObjects[businessObject.id];
      formData = jQuery.isEmptyObject(formData) ? undefined : formData === "[]" ? undefined : formData;
      if(formData !== undefined && !internal){
        //This is a necessary thing, otherwise setData will not work correctly
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
