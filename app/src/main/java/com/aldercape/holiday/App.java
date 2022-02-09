package com.aldercape.holiday;

import com.aldercape.holiday.directreport.DatabaseDirectReportRepository;
import com.aldercape.holiday.directreport.DirectReportsController;
import com.aldercape.holiday.event.DatabaseEventRepository;
import com.aldercape.holiday.event.EventController;

import java.nio.file.Path;

public class App {
    private DatabaseEventRepository eventRepository;
    private HolidayServer server;
    private HolidayDatabase database;

    public App(HolidayServer server, HolidayDatabase database, DatabaseEventRepository eventRepository) {
        this.server = server;
        this.database = database;
        this.eventRepository = eventRepository;
    }

    public static void main(String[] args) {
        var database = new HolidayDatabase();
        var eventRepository = new DatabaseEventRepository(database);
        var eventsController = new EventController(eventRepository);
        var directReportRepository = new DatabaseDirectReportRepository(database);
        var directReportsController = new DirectReportsController(directReportRepository);
        var routingServlet = new RoutingServlet(eventsController, directReportsController);
        var server = new HolidayServer(routingServlet);

        new App(server, database, eventRepository).start(Path.of(args[0]));
    }

    void start(Path path) {
        database.start(path);
        eventRepository.createTables();
        server.startServer(8080);
    }

    public void stop() throws Exception {
        server.stopServer();
    }
}
