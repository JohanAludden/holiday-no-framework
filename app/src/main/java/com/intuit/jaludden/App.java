package com.intuit.jaludden;

public class App {
    private HolidayServer server;

    public App(HolidayServer server) {
        this.server = server;
    }

    public static void main(String[] args) throws Exception {
        new App(new HolidayServer()).start();
    }

    void start() throws Exception {
        server.startServer(8080);
    }

    public void stop() throws Exception {
        server.stopServer();
    }
}
