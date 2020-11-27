package com.intuit.jaludden;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HolidayDatabaseTest {

    @TempDir
    public Path tempDir;

    @Test
    public void testStartsAndStopsDatabase() {
        var database = new HolidayDatabase();
        database.start(tempDir.resolve("sqlite.db"));
        database.stop();

        database.start(tempDir.resolve("sqlite.db"));
        database.stop();
    }

    @Test
    public void testKnowsRunningState() {
        var database = new HolidayDatabase();
        assertFalse(database.isStarted());
        database.start(tempDir.resolve("sqlite.db"));
        assertTrue(database.isStarted());
        database.stop();
        assertFalse(database.isStarted());
    }


    @Test
    public void failsGracefullyOnStartupErrors() throws Exception {
        var database = new HolidayDatabase();
        var e = assertThrows(RuntimeException.class, () -> database.start(tempDir.resolve("path_that_does_not_exist").resolve("sqlite.db")));
        assertEquals("Can't start database due to error", e.getMessage());
    }

    @Test
    public void failsFastIfDatabaseIsStartedTwice() throws Exception {
        startAndStopServer(database -> {
            var e = assertThrows(RuntimeException.class, () -> database.start(tempDir.resolve("sqlite.db")));
            assertEquals("can't start database because it's already running", e.getMessage());
        });
    }

    @Test
    public void failsFastIfDatabaseIsStoppedBeforeStarted() {
        var database = new HolidayDatabase();
        var e = assertThrows(RuntimeException.class, database::stop);
        assertEquals("can't stop database because it's not started", e.getMessage());
    }

    @Test
    public void testNullability() {
        var database = HolidayDatabase.createNull();
        database.start(tempDir.resolve("asas").resolve("sqlite.db"));
    }

    @Test
    public void realDatabaseCanExecuteQueryAndCallsCallback() throws Exception {
        startAndStopServer(database -> {
            var callbackCalled = new AtomicBoolean(false);
            database.executeQuery("SELECT 'Hello world'", rs -> callbackCalled.set(true));
            assertTrue(callbackCalled.get());
        });
    }

    @Test
    public void failsFastIfExecuteQueryIsCalledWhenStopped() {
        var database = new HolidayDatabase();
        var e = assertThrows(
                RuntimeException.class,
                () -> database.executeQuery("statement doesnt matter", rs -> {
                }));
        assertEquals("can't query a stopped database", e.getMessage());
    }

    @Test
    public void realDatabaseCanExecuteInsertAndQueryTheResult() throws Exception {
        startAndStopServer(database -> {
            var result = new AtomicReference<>();
            database.createTable("CREATE TABLE IF NOT EXISTS employee (name text)");
            database.executeInsert("INSERT into employee (name) values (?)", "Johan");
            database.executeQuery("select name from employee", rs -> {
                if (rs.next()) {
                    result.set(rs.getString("name"));
                }
            });
            assertEquals("Johan", result.get());
        });
    }

    @Test
    public void canQueryWithParameters() throws Exception {
        startAndStopServer(database -> {
            var result = new AtomicInteger();
            database.createTable("CREATE TABLE IF NOT EXISTS employee (name text)");
            database.executeInsert("INSERT into employee (name) values (?)", "Johan");
            database.executeInsert("INSERT into employee (name) values (?)", "Varsha");
            database.executeQuery("select name from employee where name = ?", rs -> {
                if (rs.next()) {
                    result.incrementAndGet();
                }
            }, "Johan");
            assertEquals(1, result.get());
        });
    }

    @Test
    public void failsFastIfExecuteInsertIsCalledWhenStopped() {
        var database = new HolidayDatabase();
        var e = assertThrows(
                RuntimeException.class,
                () -> database.executeInsert("statement doesnt matter"));
        assertEquals("can't insert into a stopped database", e.getMessage());
    }

    @Test
    public void failsFastIfCreateTablesIsCalledWhenStopped() {
        var database = new HolidayDatabase();
        var e = assertThrows(
                RuntimeException.class,
                () -> database.createTable("statement doesnt matter"));
        assertEquals("can't create table on a stopped database", e.getMessage());
    }

    private void startAndStopServer(ConsumerWithException<HolidayDatabase> whileStarted) throws Exception {
        var database = new HolidayDatabase();
        database.start(tempDir.resolve("sqlite.db"));
        try {
            whileStarted.accept(database);
        } finally {
            database.stop();
        }
    }

    private interface ConsumerWithException<T> {
        void accept(T t) throws Exception;
    }
}
