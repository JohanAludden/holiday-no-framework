package com.aldercape.holiday.event;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventControllerTest {

    private static final LocalDate DECEMBER_TENTH = LocalDate.of(2020, 12, 10);
    private static final LocalDate DECEMBER_ELEVENTH = LocalDate.of(2020, 12, 11);

    EventRepository repository = new InMemoryEventRepository();
    private EventController controller = new EventController(repository);

    @Test
    public void testNoEvents() {
        var events = getAllEventsFor("Johan").getAllEvents();
        assertEquals(0, events.size());
    }

    @Test
    public void testAllAddedEventsAreReturned() {
        createHolidayEventFor("Johan", DECEMBER_TENTH);
        createHolidayEventFor("Johan", LocalDate.of(2020, 12, 11));
        var events = getAllEventsFor("Johan").getAllEvents();
        assertEquals(2, events.size());
    }

    @Test
    public void testOtherEmployeeEventsAreNotReturned() {
        createHolidayEventFor("Johan", DECEMBER_TENTH);
        createHolidayEventFor("Varsha", DECEMBER_ELEVENTH);

        var events = getAllEventsFor("Johan");
        assertEquals(1, events.getAllEvents().size());
        assertEquals(new Event(DECEMBER_TENTH, Event.Type.HOLIDAY), events.getAllEvents().get(0));
        assertEquals("Johan", events.getEmployee());
    }

    @Test
    public void testCreatesHolidayEvent() {
        var result = createEventFor("Johan", DECEMBER_TENTH, Event.Type.HOLIDAY);
        assertEquals(result, new Event(DECEMBER_TENTH, Event.Type.HOLIDAY));
    }

    @Test
    void testReturnsOnlyHolidays() {
        createEventFor("Johan", DECEMBER_TENTH, Event.Type.HOLIDAY);
        createEventFor("Johan", DECEMBER_ELEVENTH, Event.Type.SICK_DAY);
        var events = getAllByType("Johan", Event.Type.HOLIDAY);
        assertEquals(1, events.getAllEvents().size());
        assertEquals(new Event(DECEMBER_TENTH, Event.Type.HOLIDAY), events.getAllEvents().get(0));
    }

    @Test
    public void testCreatesSickDayEvent() {
        var result = createEventFor("Johan", DECEMBER_TENTH, Event.Type.SICK_DAY);
        assertEquals(result, new Event(DECEMBER_TENTH, Event.Type.SICK_DAY));
    }

    @Test
    void testReturnsOnlySickDays() {
        createEventFor("Johan", DECEMBER_TENTH, Event.Type.HOLIDAY);
        createEventFor("Johan", DECEMBER_ELEVENTH, Event.Type.SICK_DAY);
        var events = getAllByType("Johan", Event.Type.SICK_DAY);
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

    private static class InMemoryEventRepository implements EventRepository {

        private Map<String, Events> events = new HashMap<>();

        @Override
        public Events getAllFor(String employee) {
            return events.getOrDefault(employee, new Events(employee));
        }

        @Override
        public Events getAllForWithType(String employee, Event.Type type) {
            return getAllFor(employee).filterBy(type);
        }

        @Override
        public void addEvent(String employee, Event result) {
            events.computeIfAbsent(employee, (e) -> new Events(employee)).addEvent(result);
        }
    }
}
