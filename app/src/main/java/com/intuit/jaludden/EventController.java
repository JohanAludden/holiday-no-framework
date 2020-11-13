package com.intuit.jaludden;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class EventController {

    private Map<String, Events> events = new HashMap<>();

    public Events getEventsFor(String employee) {
        return events.getOrDefault(employee, new Events(employee));
    }

    public void createEventFor(String employee, LocalDate date) {
        events.computeIfAbsent(employee, (e) -> new Events(employee)).addEvent(new Event(date));
    }
}
