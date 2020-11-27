/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.intuit.jaludden;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AppTest {

    @BeforeEach
    public void startServer() throws Exception {
    }

    @Test
    void shouldStartServer() throws Exception {
        var server = HolidayServer.createNull();
        var database = HolidayDatabase.createNull();
        var app = new App(server, database);
        app.start(Path.of("Ignored"));
        assertTrue(server.isStarted());
    }
}
