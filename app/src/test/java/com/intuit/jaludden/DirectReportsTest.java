package com.intuit.jaludden;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DirectReportsTest {

    @Test
    public void testToJsonNoDirectReports() {
        String result = new DirectReports("Johan").toJson();
        assertEquals("{\"manager\": \"Johan\", \"direct_reports\": []}", result);
    }

    @Test
    public void testToJsonOneDirectReport() {
        DirectReports directReports = new DirectReports("Johan");
        directReports.add(new DirectReport("Alex"));

        String result = directReports.toJson();
        assertEquals("{\"manager\": \"Johan\", \"direct_reports\": [{\"name\": \"Alex\"}]}", result);
    }

    @Test
    public void testToJsonTwoDirectReports() {
        DirectReports directReports = new DirectReports("Johan");
        directReports.add(new DirectReport("Alex"));
        directReports.add(new DirectReport("Varsha"));

        String result = directReports.toJson();
        assertEquals("{\"manager\": \"Johan\", \"direct_reports\": [{\"name\": \"Alex\"},{\"name\": \"Varsha\"}]}", result);
    }
}
