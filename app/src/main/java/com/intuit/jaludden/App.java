package com.intuit.jaludden;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

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
