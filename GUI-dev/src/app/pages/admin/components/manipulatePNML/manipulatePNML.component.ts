import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { EventLoggerService } from '../../../../EventLogger.service';

@Component({
  selector: 'import',
  styles: [require('./manipulatePNML.scss')],
  template: require('./manipulatePNML.html')
})
export class ManipulatePNML implements OnInit {
   error = undefined;
   pnmlFile;
   csvFile;

  constructor(protected service:EventLoggerService) {}

  ngOnInit(): void {
  }

  onPNMLFileChange(event) {
    var that = this;
    this.pnmlFile = event.srcElement.files[0];
    var split = this.pnmlFile.name.split(".");
    if(split[split.length-1] !== "pnml") {
      this.pnmlFile = undefined;
      event.target.value = "";
    }
  }

  onCSVFileChange(event) {
    var that = this;
    this.csvFile = event.srcElement.files[0];
    var split = this.csvFile.name.split(".");
    if(split[split.length-1] !== "csv") {
      this.csvFile = undefined;
      event.target.value = "";
    }
  }

  uploadFiles(form):void {
    var that = this;
    var pnmlReader = new FileReader();
    var csvReader = new FileReader();
    var newFileName = "";
    if(this.pnmlFile) {
      pnmlReader.onload = (e) => csvReader.readAsText(this.csvFile);
      csvReader.onload = (e) => {
        that.service.manipulatePNML(pnmlReader.result, csvReader.result)
        .subscribe(
            data => {
              that.saveData(data, newFileName);
              that.error = undefined;
            },
            err => {
              that.error = "Die PNML Datei konnte nicht richtig interpretiert werden! " + JSON.parse(err._body).message;
            }
          );
      }
      newFileName = this.pnmlFile.name.replace(".pnml", "-manipulated.pnml");
      pnmlReader.readAsText(this.pnmlFile);
    }
  }

  saveData(data, fileName) {
    var a = document.createElement("a");
    document.body.appendChild(a);
    a.style.cssText = "display: none";
    let blob = new Blob([data._body], {type: "application/xml"});
    let url = window.URL.createObjectURL(blob);
    a.href = url;
    a.download = fileName;
    a.click();
    window.URL.revokeObjectURL(url);
  }
}
