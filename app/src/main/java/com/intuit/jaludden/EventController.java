package com.intuit.jaludden;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class EventController {

    private Map<String, Events> events = new HashMap<>();

    public Events getEventsFor(String employee) {
        return events.getOrDefault(employee, new Events(employee));
    }

    public Events getEventsFor(String employee, Event.Type type) {
        return events.getOrDefault(employee, new Events(employee)).filterBy(type);
    }

    public Event createEventFor(String employee, LocalDate date, Event.Type type) {
        Event result = new Event(date, type);
        events.computeIfAbsent(employee, (e) -> new Events(employee)).addEvent(result);
        return result;
    }

}
