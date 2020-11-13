package com.intuit.jaludden;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EventController {

    private Map<String, List<Events>> events = new HashMap<>();

    public List<Events> getEventsFor(String employee) {
        return events.getOrDefault(employee, Collections.emptyList());
    }

    public void createEventFor(String employee, LocalDate date) {
        events.computeIfAbsent(employee, (e) -> new LinkedList<>()).add(new Events(date));
    }
}
