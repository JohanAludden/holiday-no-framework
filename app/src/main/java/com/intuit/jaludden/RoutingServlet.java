package com.intuit.jaludden;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RoutingServlet extends HttpServlet {
    EventController eventsController = new EventController();

    public static class RoutingResult {
        public int status;
        public String body;

        public RoutingResult(int status) {
            this.status = status;
        }

        public RoutingResult(int status, String body) {
            this.status = status;
            this.body = body;
        }
    }

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var result = route(
                req.getMethod(),
                req.getPathInfo(),
                req.getParameterMap()
        );
        resp.setStatus(result.status);
        resp.setContentType("application/json");
        if (result.body != null) {
            resp.getWriter().print(result.body);
        }
    }

    public RoutingResult route(String method, String path, Map<String, String[]> parameters) {
        System.out.printf("Method: %s path: %s\n", method, path);
        String[] pathElements = path.split("/");
        if (pathElements.length == 0) {
            return new RoutingResult(200);
        }
        if (pathElements[1].equals("events")) {
            String employee = pathElements[2];
            if (pathElements.length == 3) {
                switch (method) {
                    case "GET":
                        var result = eventsController.getEventsFor(employee);
                        return new RoutingResult(200, result.toJson());
                    case "POST":
                        LocalDate date = LocalDate.parse(parameters.get("date")[0]);
                        Event.Type type = Event.Type.valueOf(parameters.get("type")[0]);
                        var event = eventsController.createEventFor(employee, date, type);
                        return new RoutingResult(201, event.toJson());
                }
            } else if (pathElements.length == 4 && pathElements[3].equals("direct_reports")) {
                switch (method) {
                    case "GET":
                        return new RoutingResult(200);
                    case "POST":
                        return new RoutingResult(404);
                }
            }
        }
        return new RoutingResult(404);
    }
}
