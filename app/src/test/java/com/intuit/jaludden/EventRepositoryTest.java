package com.intuit.jaludden;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventRepositoryTest {

    @Test
    public void testEmptyRepository() {
        HolidayDatabase db = HolidayDatabase.createNull();
        db.start(Path.of("ignored"));
        EventRepository repository = new EventRepository(db);
        Events result = repository.getAllFor("Johan");
        assertEquals("Johan", result.getEmployee());
        assertEquals(Collections.emptyList(), result.getAllEvents());
    }

    @Test
    public void testOneEntry() {
        HolidayDatabase db = HolidayDatabase.createNull();
        db.start(Path.of("ignored"));
        EventRepository repository = new EventRepository(db);
        repository.addEvent("Johan", new Event(LocalDate.of(2020, 12, 10), Event.Type.HOLIDAY));
        Events result = repository.getAllFor("Johan");
        assertEquals("Johan", result.getEmployee());
        assertEquals(Collections.singletonList(new Event(LocalDate.of(2020, 12, 10), Event.Type.HOLIDAY)), result.getAllEvents());
    }
}
