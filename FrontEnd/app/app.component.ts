import {Component} from 'angular2/core';
import {CalendarComponent} from './calendar/calendar.component';
import {CalendarService} from './calendar/calendar.service';

@Component({
    selector: 'my-app',
    //template: '<h1>My First Angular 2 App</h1>',
    templateUrl: "app/app.component.html",
    directives: [CalendarComponent],
    providers: [CalendarService]
})
export class AppComponent { }