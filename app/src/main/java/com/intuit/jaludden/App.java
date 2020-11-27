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
        var database = new HolidayDatabase();
        var eventRepository = new DatabaseEventRepository(database);
        var eventsController = new EventController(eventRepository);
        var directReportsController = new DirectReportsController();
        var routingServlet = new RoutingServlet(eventsController, directReportsController);
        var server = new HolidayServer(routingServlet);

        new App(server, database).start(Path.of(args[0]));
    }

    void start(Path path) {
        database.start(path);
        server.startServer(8080);
    }

    public void stop() throws Exception {
        server.stopServer();
    }
}
