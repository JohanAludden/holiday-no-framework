package com.intuit.jaludden;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DatabaseEventRepository implements EventRepository {

    private final HolidayDatabase database;

    public DatabaseEventRepository(HolidayDatabase database) {
        this.database = database;
    }

    @Override
    public Events getAllFor(String employee) {
        var result = new Events(employee);
        database.executeQuery("select name, date, type from events where name = ?", rs -> {
            while (rs.next()) {
                result.addEvent(new Event(LocalDate.parse(rs.getString("date")), Event.Type.valueOf(rs.getString("type"))));
            }
        });
        return result;
    }

    @Override
    public Events getAllForWithType(String employee, Event.Type type) {
        return getAllFor(employee).filterBy(type);
    }

    @Override
    public void addEvent(String employee, Event result) {
        database.executeInsert(
                "insert into events (name, date, type) values (?, ?, ?)",
                employee,
                result.date().format(DateTimeFormatter.ISO_DATE),
                result.type().name());
    }
}
