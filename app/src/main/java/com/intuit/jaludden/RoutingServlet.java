package com.intuit.jaludden;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
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
        var reader = req.getReader();
        var result = route(req.getMethod(), req.getPathInfo(), () -> reader.lines().collect(Collectors.joining()));
        resp.setStatus(result.status);
        resp.setContentType("application/json");
        if (result.body != null) {
            resp.getWriter().print(result.body);
        }
    }

    public RoutingResult route(String method, String path, Supplier<String> body) {
        System.out.printf("Method: %s path: %s\n", method, path);
        if (path.equals("/")) {
            return new RoutingResult(200);
        }
        if (path.startsWith("/events/")) {
            String employee = path.substring(8);
            switch (method) {
                case "GET":
                    var result = eventsController.getEventsFor(employee);
                    return new RoutingResult(200, result.toJson());
                case "POST":
                    LocalDate date = LocalDate.parse(body.get().substring(10, 20));
                    eventsController.createEventFor(employee, date);
                    return new RoutingResult(201);
            }
        }
        return new RoutingResult(404);
    }
}
