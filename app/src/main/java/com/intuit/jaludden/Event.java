package com.intuit.jaludden;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Event {

    private final LocalDate date;
    private final Type type;

    public Event(LocalDate date, Type type) {
        this.date = date;
        this.type = type;
    }

    public String toJson() {
        return String.format("{\"date\": \"%s\", \"type\": \"%s\"}", date.format(DateTimeFormatter.ISO_DATE), type.name().toLowerCase());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(date, event.date) && type == type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }

    public boolean isType(Type type) {
        return this.type == type;
    }

    public enum Type {
        HOLIDAY, SICK_DAY
    }
}
