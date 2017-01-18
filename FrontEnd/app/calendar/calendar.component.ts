import {Component} from 'angular2/core';
import {CalendarService} from './calendar.service';

@Component({
    selector: 'calendar',
    //template: '<h1>My First Angular 2 App</h1>',
    templateUrl: "app/calendar/calendar.component.html"
})
export class CalendarComponent {
    entries;

    constructor(calendarService: CalendarService) {
        this.entries = calendarService.getEntries();
    }
}