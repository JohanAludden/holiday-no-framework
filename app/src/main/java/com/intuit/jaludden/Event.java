package com.intuit.jaludden;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Event {

    private final LocalDate date;

    public Event(LocalDate date) {
        this.date = date;
    }

    public String toJson() {
        return String.format("{\"date\": \"%s\"}", date.format(DateTimeFormatter.ISO_DATE));
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(date, event.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }
}
