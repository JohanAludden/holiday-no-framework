package com.intuit.jaludden;

public interface EventRepository {
    Events getAllFor(String employee);

    Events getAllForWithType(String employee, Event.Type type);

    void addEvent(String employee, Event result);
}
