package com.intuit.jaludden;

import java.nio.file.Path;

public class App {
    private HolidayServer server;
    private HolidayDatabase database;

    public App(HolidayServer server, HolidayDatabase database) {
        this.server = server;
        this.database = database;
    }

    public static void main(String[] args) {
        new App(new HolidayServer(), new HolidayDatabase()).start(Path.of(args[0]));
    }

    void start(Path path) {
        database.start(path);
        server.startServer(8080, database);
    }

    public void stop() throws Exception {
        server.stopServer();
    }
}
