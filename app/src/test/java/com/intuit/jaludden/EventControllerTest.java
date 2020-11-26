package com.intuit.jaludden;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventControllerTest {

    private static final LocalDate DECEMBER_TENTH = LocalDate.of(2020, 12, 10);
    private static final LocalDate DECEMBER_ELEVENTH = LocalDate.of(2020, 12, 11);

    private EventController controller = new EventController(new EventRepository());

    @Test
    public void testNoEvents() {
        List<Event> events = getAllEventsFor("Johan").getAllEvents();
        assertEquals(0, events.size());
    }

    @Test
    public void testAllAddedEventsAreReturned() {
        createHolidayEventFor("Johan", DECEMBER_TENTH);
        createHolidayEventFor("Johan", LocalDate.of(2020, 12, 11));
        List<Event> events = getAllEventsFor("Johan").getAllEvents();
        assertEquals(2, events.size());
    }

    @Test
    public void testOtherEmployeeEventsAreNotReturned() {
        createHolidayEventFor("Johan", DECEMBER_TENTH);
        createHolidayEventFor("Varsha", DECEMBER_ELEVENTH);

        Events events = getAllEventsFor("Johan");
        assertEquals(events.getAllEvents().size(), 1);
        assertEquals(events.getAllEvents().get(0), new Event(DECEMBER_TENTH, Event.Type.HOLIDAY));
        assertEquals("Johan", events.getEmployee());
    }

    @Test
    public void testCreatesHolidayEvent() {
        Event result = createEventFor("Johan", DECEMBER_TENTH, Event.Type.HOLIDAY);
        assertEquals(result, new Event(DECEMBER_TENTH, Event.Type.HOLIDAY));
    }

    @Test
    void testReturnsOnlyHolidays() {
        createEventFor("Johan", DECEMBER_TENTH, Event.Type.HOLIDAY);
        createEventFor("Johan", DECEMBER_ELEVENTH, Event.Type.SICK_DAY);
        Events events = getAllByType("Johan", Event.Type.HOLIDAY);
        assertEquals(1, events.getAllEvents().size());
        assertEquals(new Event(DECEMBER_TENTH, Event.Type.HOLIDAY), events.getAllEvents().get(0));
    }

    @Test
    public void testCreatesSickDayEvent() {
        Event result = createEventFor("Johan", DECEMBER_TENTH, Event.Type.SICK_DAY);
        assertEquals(result, new Event(DECEMBER_TENTH, Event.Type.SICK_DAY));
    }

    @Test
    void testReturnsOnlySickDays() {
        createEventFor("Johan", DECEMBER_TENTH, Event.Type.HOLIDAY);
        createEventFor("Johan", DECEMBER_ELEVENTH, Event.Type.SICK_DAY);
        Events events = getAllByType("Johan", Event.Type.SICK_DAY);
        assertEquals(1, events.getAllEvents().size());
        assertEquals(new Event(DECEMBER_ELEVENTH, Event.Type.SICK_DAY), events.getAllEvents().get(0));
    }

    private void createHolidayEventFor(String employee, LocalDate date) {
        createEventFor(employee, date, Event.Type.HOLIDAY);
    }

    private Event createEventFor(String employee, LocalDate date, Event.Type type) {
        return controller.createEventFor(employee, date, type);
    }

    private Events getAllEventsFor(String employee) {
        return controller.getEventsFor(employee);
    }

    private Events getAllByType(String employee, Event.Type type) {
        return controller.getEventsFor(employee, type);
    }
}
