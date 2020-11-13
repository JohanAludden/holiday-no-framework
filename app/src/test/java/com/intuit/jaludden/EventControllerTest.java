package com.intuit.jaludden;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventControllerTest {

    private EventController controller = new EventController();

    @Test
    public void testNoEvents() {
        List<Event> events = controller.getEventsFor("Johan").getAllEvents();
        assertEquals(0, events.size());
    }

    @Test
    public void testAddedEventIsReturned() {
        controller.createEventFor("Johan", LocalDate.of(2020, 12, 10));
        List<Event> events = controller.getEventsFor("Johan").getAllEvents();
        assertEquals(1, events.size());
    }

    @Test
    public void testAllAddedEventsAreReturned() {
        controller.createEventFor("Johan", LocalDate.of(2020, 12, 10));
        controller.createEventFor("Johan", LocalDate.of(2020, 12, 11));
        List<Event> events = controller.getEventsFor("Johan").getAllEvents();
        assertEquals(2, events.size());
    }

    @Test
    public void testOtherEmployeeEventsAreNotReturned() {
        controller.createEventFor("Johan", LocalDate.of(2020, 12, 10));
        controller.createEventFor("Varsha", LocalDate.of(2020, 12, 11));
        Events events = controller.getEventsFor("Johan");
        assertEquals(events.getAllEvents().size(), 1);
        assertEquals(events.getAllEvents().get(0), new Event(LocalDate.of(2020, 12, 10)));
        assertEquals("Johan", events.getEmployee());
    }
}
