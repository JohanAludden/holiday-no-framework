package com.intuit.jaludden;

import java.time.LocalDate;

public class EventController {

    private EventRepository repository;

    public EventController(EventRepository repository) {
        this.repository = repository;
    }

    public Events getEventsFor(String employee) {
        return repository.getAllFor(employee);
    }

    public Events getEventsFor(String employee, Event.Type type) {
        return repository.getAllForWithType(employee, type, this);
    }

    public Event createEventFor(String employee, LocalDate date, Event.Type type) {
        var result = new Event(date, type);
        repository.addEvent(employee, result);
        return result;
    }
}
