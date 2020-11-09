package com.intuit.jaludden;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        handler.addServletWithMapping(MyServlet.class, "/*");

        server.start();
        server.join();
    }

    public void stop() throws Exception {
        server.stop();
    }

    public static class MyServlet extends HttpServlet {
        protected void service(HttpServletRequest req, HttpServletResponse resp) {
            System.out.printf("Method: %s path: %s Query: %s\n", req.getMethod(), req.getPathInfo(), req.getQueryString());
        }
    }
}
