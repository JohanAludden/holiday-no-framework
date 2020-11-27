package com.intuit.jaludden;

import java.util.HashMap;
import java.util.Map;

public class EventRepository {

    private final HolidayDatabase database;
    private Map<String, Events> events;

    public EventRepository(HolidayDatabase database) {
        this.database = database;
        this.events = new HashMap<>();
    }

    public Events getAllFor(String employee) {
//        Events result = new Events(employee);
//        database.executeQuery("select name, date, type from events where name = ?", rs -> {
//            while (rs.next()) {
//                result.addEvent(new Event(LocalDate.parse(rs.getString("date")), Event.Type.valueOf(rs.getString("type"))));
//            }
//        });
        return events.getOrDefault(employee, new Events(employee));
    }

    Events getAllForWithType(String employee, Event.Type type) {
        return getAllFor(employee).filterBy(type);
    }

    void addEvent(String employee, Event result) {
        database.executeInsert("insert into events (name, date, type) values (?, ?, ?)", employee, result.date(), result.type());
        events.computeIfAbsent(employee, (e) -> new Events(employee)).addEvent(result);
    }
}
