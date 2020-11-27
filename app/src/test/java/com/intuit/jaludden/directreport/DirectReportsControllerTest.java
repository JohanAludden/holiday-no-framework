package com.intuit.jaludden.directreport;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DirectReportsControllerTest {

    private DirectReportsController controller = new DirectReportsController(new InMemoryDirectReportRepository());

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

    private static class InMemoryDirectReportRepository implements DirectReportRepository {
        private Map<String, DirectReports> orgStructure = new HashMap<>();

        public DirectReports getDirectReportsFor(String manager) {
            return orgStructure.getOrDefault(manager, new DirectReports(manager));
        }

        public void addDirectReportFor(DirectReport directReport) {
            orgStructure.computeIfAbsent(directReport.getManagerName(), key -> new DirectReports(directReport.getManagerName())).add(directReport);
        }
    }
}
