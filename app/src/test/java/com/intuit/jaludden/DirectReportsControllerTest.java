package com.intuit.jaludden;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DirectReportsControllerTest {

    private DirectReportsController controller = new DirectReportsController();

    @Test
    public void testNoDirectReports() {
        List<DirectReport> directReports = controller.getDirectReportsFor("Johan");
        assertEquals(0, directReports.size());
    }
}
