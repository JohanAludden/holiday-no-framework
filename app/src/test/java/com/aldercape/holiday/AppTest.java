/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.aldercape.holiday;

import com.aldercape.holiday.event.DatabaseEventRepository;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AppTest {

    @Test
    void shouldStartServer() throws Exception {
        var server = HolidayServer.createNull();
        var database = HolidayDatabase.createNull();
        var app = new App(server, database, new DatabaseEventRepository(database));
        app.start(Path.of("Ignored"));
        assertTrue(server.isStarted());
    }
}
