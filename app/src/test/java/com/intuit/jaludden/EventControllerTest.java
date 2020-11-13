package com.intuit.jaludden;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventControllerTest {

    private EventController controller = new EventController();

    @Test
    public void testNoEvents() {
        List<Events> events = controller.getEventsFor("Johan");
        assertEquals(events.size(), 0);
    }

    @Test
    public void testAddedEventIsReturned() {
        controller.createEventFor("Johan", LocalDate.of(2020, 12, 10));
        List<Events> events = controller.getEventsFor("Johan");
        assertEquals(events.size(), 1);
    }

    @Test
    public void testAllAddedEventsAreReturned() {
        controller.createEventFor("Johan", LocalDate.of(2020, 12, 10));
        controller.createEventFor("Johan", LocalDate.of(2020, 12, 11));
        List<Events> events = controller.getEventsFor("Johan");
        assertEquals(events.size(), 2);
    }
    @Test
    public void testOtherEmployeeEventsAreNotReturned() {
        controller.createEventFor("Johan", LocalDate.of(2020, 12, 10));
        controller.createEventFor("Varsha", LocalDate.of(2020, 12, 11));
        List<Events> events = controller.getEventsFor("Johan");
        assertEquals(events.size(), 1);
        assertEquals(events.get(0), new Events(LocalDate.of(2020, 12, 10)));
    }
}
