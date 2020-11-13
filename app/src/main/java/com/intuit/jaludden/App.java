package com.intuit.jaludden;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.stream.Collectors;

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
        EventController eventsController = new EventController();
        protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            System.out.printf("Method: %s path: %s Query: %s\n", req.getMethod(), req.getPathInfo(), req.getQueryString());
            if (req.getPathInfo().equals("/")) {
                return;
            }
            if (req.getPathInfo().startsWith("/events/")) {
                String employee = req.getPathInfo().substring(8);
                if (req.getMethod().equals("GET")) {
                    var result = eventsController.getEventsFor(employee);
                    resp.setContentType("application/json");
                    resp.getWriter().print(result.toJson());
                    return;
                } else if (req.getMethod().equals("POST")) {
                    LocalDate date = LocalDate.parse(req.getReader().lines().collect(Collectors.joining()).substring(10, 20));
                    eventsController.createEventFor(employee, date);
                    resp.setStatus(201);
                    return;
                }
            }
            resp.setStatus(404);
        }
    }
}
