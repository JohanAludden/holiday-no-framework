package com.aldercape.holiday.directreport;

import com.aldercape.holiday.HolidayDatabase;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseDirectReportRepositoryTest {

    private HolidayDatabase db;

    @Test
    public void testEmptyRepository() {
        var repository = initiateRepository(Collections.emptyIterator());
        var result = repository.getDirectReportsFor("Johan");
        assertEquals("Johan", result.manager());
        assertEquals(Collections.emptyList(), result.directReports());
    }

    @Test
    public void testOneEntry() {
        var repository = initiateRepository(createIteratorOf(new Object[][]{
                {"manager", "Varsha"},
                {"employee", "Johan"}}));
        var result = repository.getDirectReportsFor("Varsha");
        var currentDb = getNullDatabase();
        assertEquals("select manager, employee from direct_reports where manager = ?", currentDb.statements.get(0).sql);
        assertEquals("Varsha", result.manager());
        assertEquals(1, result.directReports().size());
    }

    @Test
    public void testTwoEntries() {
        var repository = initiateRepository(createIteratorOf(
                new Object[][]{
                        {"manager", "Varsha"},
                        {"employee", "Johan"}},
                new Object[][]{
                        {"manager", "Varsha"},
                        {"employee", "Alex"}
                }));
        var result = repository.getDirectReportsFor("Varsha");
        assertEquals("Varsha", result.manager());
        assertEquals(2, result.directReports().size());
    }

    @Test
    public void testCreateDirectReport() {
        var repository = initiateRepository(Collections.emptyIterator());
        repository.addDirectReportFor(new DirectReport("Johan", "Varsha"));
        var currentDb = getNullDatabase();

        assertEquals("insert into direct_reports (manager, employee) values (?, ?)", currentDb.statements.get(0).sql);
        assertEquals(2, currentDb.statements.get(0).parameters.size());
        assertEquals("Varsha", currentDb.statements.get(0).parameters.get(1));
        assertEquals("Johan", currentDb.statements.get(0).parameters.get(2));
    }

    Iterator<Map<String, Object>> createIteratorOf(Object[][]... values) {
        return Arrays.stream(values).map(v ->
                Stream.of(v).collect(Collectors.toMap(data -> (String) data[0], data -> data[1])))
                .iterator();
    }

    private DatabaseDirectReportRepository initiateRepository(Iterator<Map<String, Object>> objectIterator) {
        db = HolidayDatabase.createNull(objectIterator);
        db.start(Path.of("ignored"));
        return new DatabaseDirectReportRepository(db);
    }

    private HolidayDatabase.NullDatabase getNullDatabase() {
        return (HolidayDatabase.NullDatabase) db.getCurrentDatabase();
    }
}
