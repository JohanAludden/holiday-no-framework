package com.intuit.jaludden;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class Events {

    private List<Event> events = new LinkedList<>();
    private String employee;

    public Events(String employee) {
        this.employee = employee;
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
}
