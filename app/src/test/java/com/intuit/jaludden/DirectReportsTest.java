package com.intuit.jaludden;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DirectReportsTest {

    @Test
    public void testToJsonNoDirectReports() {
        String result = new DirectReports("Johan", Collections.emptyList()).toJson();
        assertEquals("{\"manager\": \"Johan\", \"direct_reports\": []}", result);
    }

    @Test
    public void testToJsonOneDirectReport() {
        DirectReports events = new DirectReports("Johan", Arrays.asList(new DirectReport("Alex")));
//        events.addEvent(new Event(LocalDate.of(2020, 12, 10), Event.Type.HOLIDAY));

        String result = events.toJson();
        assertEquals("{\"manager\": \"Johan\", \"direct_reports\": [{\"name\": \"Alex\"}]}", result);
    }

    @Test
    public void testToJsonTwoDirectReports() {
        DirectReports events = new DirectReports("Johan", Arrays.asList(new DirectReport("Alex"), new DirectReport("Varsha")));
//        events.addEvent(new Event(LocalDate.of(2020, 12, 10), Event.Type.HOLIDAY));
//        events.addEvent(new Event(LocalDate.of(2020, 12, 11), Event.Type.HOLIDAY));
        String result = events.toJson();
        assertEquals("{\"manager\": \"Johan\", \"direct_reports\": [{\"name\": \"Alex\"},{\"name\": \"Varsha\"}]}", result);
    }
}
