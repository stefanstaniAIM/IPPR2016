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

  constructor(protected service:EventLoggerService) {}

  ngOnInit(): void {
  }

  onFileChange(event) {
    var that = this;
    this.pnmlFile = event.srcElement.files[0];
    var split = this.pnmlFile.name.split(".");
    if(split[split.length-1] !== "pnml") {
      this.pnmlFile = undefined;
      event.target.value = "";
    }
  }

  uploadPNMLFile(form):void {
    var that = this;
    var reader = new FileReader();
    if(this.pnmlFile) {
      reader.onload = function(e) {
        that.service.manipulatePNML(reader.result)
        .subscribe(
            data => {
              that.saveData(data, "test.pnml");
            },
            err => that.error = "Die PNML Datei konnte nicht richtig interpretiert werden!",
            () => {
            }
          );
      }
      reader.readAsText(this.pnmlFile);
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
