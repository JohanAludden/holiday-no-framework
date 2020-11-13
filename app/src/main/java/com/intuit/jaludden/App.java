package com.intuit.jaludden;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

public class App {
    private Server server;

    public static void main(String[] args) throws Exception {
        new App().serve();
    }

    void serve() throws Exception {
        server = new Server();
        var connector = new ServerConnector(server);
        connector.setPort(8080);
        server.setConnectors(new Connector[]{connector});

        var handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(RoutingServlet.class, "/*");

        server.start();
        server.join();
    }

    public void stop() throws Exception {
        server.stop();
    }
}
