package com.intuit.jaludden;

import java.util.HashMap;
import java.util.Map;

public class EventRepository {

    private Map<String, Events> events;

    public EventRepository() {
        this.events = new HashMap<>();
    }

    public Events getAllFor(String employee) {
        return events.getOrDefault(employee, new Events(employee));
    }

    Events getAllForWithType(String employee, Event.Type type, EventController eventController) {
        return getAllFor(employee).filterBy(type);
    }

    void addEvent(String employee, Event result) {
        events.computeIfAbsent(employee, (e) -> new Events(employee)).addEvent(result);
    }
}
