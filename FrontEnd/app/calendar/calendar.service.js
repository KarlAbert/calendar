System.register([], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var CalendarService;
    return {
        setters:[],
        execute: function() {
            CalendarService = (function () {
                function CalendarService() {
                }
                CalendarService.prototype.getEntries = function () {
                    return [{ name: "Hier wird" }, { name: "auf REST" }, { name: "zugegriffen" }];
                };
                return CalendarService;
            }());
            exports_1("CalendarService", CalendarService);
        }
    }
});
//# sourceMappingURL=calendar.service.js.map