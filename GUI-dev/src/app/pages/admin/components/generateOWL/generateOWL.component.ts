import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { EventLoggerService } from '../../../../EventLogger.service';

@Component({
  selector: 'import',
  styles: [require('./generateOWL.scss')],
  template: require('./generateOWL.html')
})
export class GenerateOWL implements OnInit {
   error = undefined;
   pnmlFiles = [{id: 1, name: "", file: undefined}, {id: 2, name: "", file: undefined}];

  constructor(protected service:EventLoggerService) {}

  ngOnInit(): void {
  }

  addFile() {
    this.pnmlFiles.push({id: this.pnmlFiles.length+1, name: "", file: undefined});
  }

  onPNMLFileChange(event, id) {
    var that = this;
    var pnmlFile = that.pnmlFiles.filter(x => x.id === id)[0];
    pnmlFile.file = event.srcElement.files[0];
    var split = pnmlFile.file.name.split(".");
    if(split[split.length-1] !== "pnml") {
      pnmlFile = undefined;
      event.target.value = "";
    }
  }

  uploadFiles(form):void {
    var that = this;
    var newFileName = "test.owl";
    var fileResults = {}
    var fileReaderPromises = [];
    that.pnmlFiles.forEach(p => {
      if (!p.name || p.name === "" || !p.file) {
        return;
      }
      var promise = new Promise((resolve, reject) => {
        var pnmlReader = new FileReader();
        pnmlReader.onload = (e) => {
          fileResults[p.name] = pnmlReader.result;
          resolve();
        }
        pnmlReader.readAsText(p.file)
      });
      fileReaderPromises.push(promise);
    });
    Promise.all(fileReaderPromises).then(result => {
      that.service.generateOWL("Testinger", fileResults)
      .subscribe(
          data => {
            that.saveData(data, newFileName);
            that.error = undefined;
          },
          err => {
            that.error = "Die OWL Datei konnte nicht generiert werden! " + JSON.parse(err._body).message;
          }
        );
    })

  /*  var that = this;
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
    }*/
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
