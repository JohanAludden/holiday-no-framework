package com.intuit.jaludden.event;

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
        events.addEvent(new Event(LocalDate.of(2020, 12, 10), Event.Type.HOLIDAY));

        String result = events.toJson();
        assertEquals("{\"employee\": \"Johan\", \"events\": [{\"date\": \"2020-12-10\", \"type\": \"holiday\"}]}", result);
    }

    @Test
    public void testToJsonTwoEvents() {
        Events events = new Events("Johan");
        events.addEvent(new Event(LocalDate.of(2020, 12, 10), Event.Type.HOLIDAY));
        events.addEvent(new Event(LocalDate.of(2020, 12, 11), Event.Type.HOLIDAY));
        String result = events.toJson();
        assertEquals("{\"employee\": \"Johan\", \"events\": [{\"date\": \"2020-12-10\", \"type\": \"holiday\"},{\"date\": \"2020-12-11\", \"type\": \"holiday\"}]}", result);
    }
}
