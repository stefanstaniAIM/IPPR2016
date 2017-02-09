import { Pipe, PipeTransform } from '@angular/core';

@Pipe({name: 'commaDate'})
export class CommaDatePipe implements PipeTransform {
  transform(values: string, args: string[]): Date {
    var month = parseInt(values[1]) || 0;
    return new Date(parseInt(values[0]) || 0, month > 0 ? month -1 : month, parseInt(values[2]) || 0, parseInt(values[3]) || 0, parseInt(values[4]) || 0, parseInt(values[5]) || 0);
  }
}
