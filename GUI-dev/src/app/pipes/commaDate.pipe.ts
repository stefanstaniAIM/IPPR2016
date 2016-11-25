import { Pipe, PipeTransform } from '@angular/core';

@Pipe({name: 'commaDate'})
export class CommaDatePipe implements PipeTransform {
  transform(values: string, args: string[]): Date {
    return new Date(parseInt(values[0]), parseInt(values[1]), parseInt(values[2]), parseInt(values[3]), parseInt(values[4]), parseInt(values[5]));
  }
}
