package com.intuit.jaludden;

import com.intuit.jaludden.directreport.DirectReportsController;
import com.intuit.jaludden.event.Event;
import com.intuit.jaludden.event.EventController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

public class RoutingServlet extends HttpServlet {
    EventController eventsController;
    DirectReportsController directReportsController;

    public RoutingServlet(EventController eventsController, DirectReportsController directReportsController) {
        this.eventsController = eventsController;
        this.directReportsController = directReportsController;
    }

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
        var pathElements = path.split("/");
        if (pathElements.length == 0) {
            return new RoutingResult(200);
        }
        var employee = pathElements[1];
        if (pathElements[2].equals("events")) {
            switch (method) {
                case "GET":
                    var result = eventsController.getEventsFor(employee);
                    return new RoutingResult(200, result.toJson());
                case "POST":
                    var date = LocalDate.parse(parameters.get("date")[0]);
                    var type = Event.Type.valueOf(parameters.get("type")[0]);
                    var event = eventsController.createEventFor(employee, date, type);
                    return new RoutingResult(201, event.toJson());
            }
        } else if (pathElements[2].equals("direct_reports")) {
            switch (method) {
                case "GET":
                    var result = directReportsController.getDirectReportsFor(employee);
                    return new RoutingResult(200, result.toJson());
                case "POST":
                    var directReportName = parameters.get("employee_name")[0];
                    var directReport = directReportsController.addDirectReportFor(employee, directReportName);
                    return new RoutingResult(201, directReport.toJson());
            }
        }
        return new RoutingResult(404);
    }
}
