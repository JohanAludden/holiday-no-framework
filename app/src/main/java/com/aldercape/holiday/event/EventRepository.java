package com.aldercape.holiday.event;

public interface EventRepository {
    Events getAllFor(String employee);

    Events getAllForWithType(String employee, Event.Type type);

    void addEvent(String employee, Event result);
}
