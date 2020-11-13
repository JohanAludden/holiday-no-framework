package com.intuit.jaludden;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventsTest {

    @Test
    public void testToJsonNoEvents() {
        String result = new Events("Johan").toJson();
        assertEquals("{\"employee\": \"Johan\", \"events\": []}", result);
    }

    @Test
    public void testToJsonOneEvent() {
        Events events = new Events("Johan");
        events.addEvent(new Event(LocalDate.of(2020, 12, 10)));
        String result = events.toJson();
        assertEquals("{\"employee\": \"Johan\", \"events\": [{\"date\": \"2020-12-10\"}]}", result);
    }

    @Test
    public void testToJsonTwoEvents() {
        Events events = new Events("Johan");
        events.addEvent(new Event(LocalDate.of(2020, 12, 10)));
        events.addEvent(new Event(LocalDate.of(2020, 12, 11)));
        String result = events.toJson();
        assertEquals("{\"employee\": \"Johan\", \"events\": [{\"date\": \"2020-12-10\"},{\"date\": \"2020-12-11\"}]}", result);
    }
}
