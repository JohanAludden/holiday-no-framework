package com.intuit.jaludden;

import java.time.LocalDate;
import java.util.Objects;

public class Events {

    private final LocalDate date;

    public Events(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Events events = (Events) o;
        return Objects.equals(date, events.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }
}
