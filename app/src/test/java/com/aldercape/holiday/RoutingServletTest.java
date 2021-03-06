package com.aldercape.holiday;

import com.aldercape.holiday.directreport.DatabaseDirectReportRepository;
import com.aldercape.holiday.directreport.DirectReportsController;
import com.aldercape.holiday.event.DatabaseEventRepository;
import com.aldercape.holiday.event.EventController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class RoutingServletTest {

    @DisplayName("basket provides discounted value when containing...")
    @MethodSource("routingData")
    @ParameterizedTest(name = "{0} {1} ({2}) -> {3}, ({4})")
    public void testRouting(String method, String path, String[][] inputBody, int statusCode, String outputBody) {
        HolidayDatabase database = HolidayDatabase.createNull();
        database.start(Path.of("Ignored"));
        var servlet = new RoutingServlet(new EventController(new DatabaseEventRepository(database)), new DirectReportsController(new DatabaseDirectReportRepository(database)));
        Map<String, String[]> parameters = Arrays.stream(inputBody).collect(Collectors.toMap(i -> i[0], i -> new String[]{i[1]}));
        var result = servlet.route(method, path, parameters);
        assertEquals(statusCode, result.status);
        assertEquals(outputBody, result.body);
    }

    static Stream<Arguments> routingData() {
        return Stream.of(
                GET("/").expectResponse(200),

                GET("/johan/events")
                        .expectResponse(200, "{\"employee\": \"johan\", \"events\": []}"),
                POST("/johan/events", new String[][]{{"date", "2020-12-10"}, {"type", "HOLIDAY"}})
                        .expectResponse(201, "{\"date\": \"2020-12-10\", \"type\": \"holiday\"}"),
                POST("/johan/events", new String[][]{{"date", "2020-12-10"}, {"type", "SICK_DAY"}})
                        .expectResponse(201, "{\"date\": \"2020-12-10\", \"type\": \"sick_day\"}"),
                GET("/johan/does/not/exist")
                        .expectResponse(404),

                GET("/varsha/direct_reports")
                        .expectResponse(200, "{\"manager\": \"varsha\", \"direct_reports\": []}"),

                POST("/varsha/direct_reports", new String[][]{{"employee_name", "Johan"}})
                        .expectResponse(201, "{\"manager_name\": \"varsha\", \"employee_name\": \"Johan\"}"),

                GET("/a/path/that/does/not/exist")
                        .expectResponse(404)
        );
    }

    private static RequestArguments GET(String path) {
        return new RequestArguments("GET", path);
    }

    private static RequestArguments POST(String path, String[][] requestBody) {
        return new RequestArguments("POST", path, requestBody);
    }

    private static class RequestArguments {

        private final String method;
        private final String path;
        private final String[][] requestBody;

        public RequestArguments(String method, String path) {
            this(method, path, new String[][]{});
        }

        public RequestArguments(String method, String path, String[][] requestBody) {
            this.method = method;
            this.path = path;
            this.requestBody = requestBody;
        }

        public Arguments expectResponse(int statusCode) {
            return expectResponse(statusCode, null);
        }

        public Arguments expectResponse(int statusCode, String responseBody) {
            return arguments(method, path, requestBody, statusCode, responseBody);
        }
    }
}
