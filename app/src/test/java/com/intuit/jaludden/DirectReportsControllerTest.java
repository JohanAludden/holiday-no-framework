package com.intuit.jaludden;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DirectReportsControllerTest {

    private DirectReportsController controller = new DirectReportsController();

    @Test
    public void testNoDirectReports() {
        var directReports = controller.getDirectReportsFor("Varsha");
        assertEquals(0, directReports.size());
    }

    @Test
    public void testOneDirectReport() {
        controller.addDirectReportFor("Varsha", "Johan");
        var directReports = controller.getDirectReportsFor("Varsha");
        assertEquals(1, directReports.size());
        assertEquals("Johan", directReports.get(0).getName());
    }

    @Test
    public void testDoesNotIncludeOthersReports() {
        controller.addDirectReportFor("Varsha", "Johan");
        controller.addDirectReportFor("Israel", "Alex");
        var directReports = controller.getDirectReportsFor("Varsha");
        assertEquals(1, directReports.size());
        assertEquals("Johan", directReports.get(0).getName());
    }

    @Test
    public void testDoesNotIncludeSubReports() {
        controller.addDirectReportFor("Varsha", "Johan");
        controller.addDirectReportFor("Johan", "Alex");
        var directReports = controller.getDirectReportsFor("Varsha");
        assertEquals(1, directReports.size());
        assertEquals("Johan", directReports.get(0).getName());
    }
}
