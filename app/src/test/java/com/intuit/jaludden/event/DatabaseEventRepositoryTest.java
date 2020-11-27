package com.intuit.jaludden.event;

import com.intuit.jaludden.HolidayDatabase;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseEventRepositoryTest {

    private HolidayDatabase db;

    @Test
    public void testEmptyRepository() {
        var repository = initiateRepository(Collections.emptyIterator());
        var result = repository.getAllFor("Johan");
        assertEquals("Johan", result.getEmployee());
        assertEquals(Collections.emptyList(), result.getAllEvents());
    }

    @Test
    public void testOneEntry() {
        var repository = initiateRepository(createIteratorOf(new Object[][]{
                {"name", "Johan"},
                {"date", "2020-12-10"},
                {"type", Event.Type.HOLIDAY.name()}}));
        var result = repository.getAllFor("Johan");
        assertEquals("Johan", result.getEmployee());
        assertEquals(Collections.singletonList(new Event(LocalDate.of(2020, 12, 10), Event.Type.HOLIDAY)), result.getAllEvents());
    }

    @Test
    public void testAddEntry() {
        var repository = initiateRepository(Collections.emptyIterator());
        repository.addEvent("Johan", new Event(LocalDate.of(2020, 12, 10), Event.Type.HOLIDAY));
        var currentDb = getNullDatabase();

        assertEquals("insert into events (name, date, type) values (?, ?, ?)", currentDb.statements.get(0).sql);
        assertEquals(3, currentDb.statements.get(0).parameters.size());
        assertEquals("Johan", currentDb.statements.get(0).parameters.get(1));
        assertEquals("2020-12-10", currentDb.statements.get(0).parameters.get(2));
        assertEquals(Event.Type.HOLIDAY.name(), currentDb.statements.get(0).parameters.get(3));
    }

    private HolidayDatabase.NullDatabase getNullDatabase() {
        return (HolidayDatabase.NullDatabase) db.getCurrentDatabase();
    }

    Iterator<Map<String, Object>> createIteratorOf(Object[][] values) {
        return Collections.singleton(
                Stream.of(values).collect(Collectors.toMap(data -> (String) data[0], data -> data[1])))
                .iterator();
    }

    private DatabaseEventRepository initiateRepository(Iterator<Map<String, Object>> objectIterator) {
        db = HolidayDatabase.createNull(objectIterator);
        db.start(Path.of("ignored"));
        return new DatabaseEventRepository(db);
    }
}
