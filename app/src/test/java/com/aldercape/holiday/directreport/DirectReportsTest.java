package com.aldercape.holiday.directreport;

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
        directReports.add(new DirectReport("Alex", "Johan"));

        String result = directReports.toJson();
        assertEquals("{\"manager\": \"Johan\", \"direct_reports\": [{\"manager_name\": \"Johan\", \"employee_name\": \"Alex\"}]}", result);
    }

    @Test
    public void testToJsonTwoDirectReports() {
        DirectReports directReports = new DirectReports("Johan");
        directReports.add(new DirectReport("Alex", "Johan"));
        directReports.add(new DirectReport("Varsha", "Johan"));

        String result = directReports.toJson();
        assertEquals("{\"manager\": \"Johan\", \"direct_reports\": [{\"manager_name\": \"Johan\", \"employee_name\": \"Alex\"},{\"manager_name\": \"Johan\", \"employee_name\": \"Varsha\"}]}", result);
    }
}
