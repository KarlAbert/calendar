System.register(['angular2/core', './calendar.service'], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __metadata = (this && this.__metadata) || function (k, v) {
        if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
    };
    var core_1, calendar_service_1;
    var CalendarComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (calendar_service_1_1) {
                calendar_service_1 = calendar_service_1_1;
            }],
        execute: function() {
            CalendarComponent = (function () {
                function CalendarComponent(calendarService) {
                    this.entries = calendarService.getEntries();
                }
                CalendarComponent = __decorate([
                    core_1.Component({
                        selector: 'calendar',
                        //template: '<h1>My First Angular 2 App</h1>',
                        templateUrl: "app/calendar/calendar.component.html"
                    }), 
                    __metadata('design:paramtypes', [calendar_service_1.CalendarService])
                ], CalendarComponent);
                return CalendarComponent;
            }());
            exports_1("CalendarComponent", CalendarComponent);
        }
    }
});
//# sourceMappingURL=calendar.component.js.map