package com.aldercape.holiday.event;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Events {

    private List<Event> events = new LinkedList<>();
    private String employee;

    public Events(String employee) {
        this.employee = employee;
    }

    private Events(String employee, List<Event> events) {
        this.employee = employee;
        this.events = events;
    }

    public String getEmployee() {
        return employee;
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public List<Event> getAllEvents() {
        return Collections.unmodifiableList(events);
    }

    public String toJson() {
        return String.format("{\"employee\": \"%s\", \"events\": [%s]}",
                employee,
                events.stream()
                        .map(Event::toJson)
                        .collect(Collectors.joining(",")));
    }

    public Events filterBy(Event.Type type) {
        var filteredEvents = events.stream().filter(e -> e.isType(type)).collect(Collectors.toList());
        return new Events(employee, filteredEvents);
    }
}
