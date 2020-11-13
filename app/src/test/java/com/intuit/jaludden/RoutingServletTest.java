package com.intuit.jaludden;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class RoutingServletTest {

    @DisplayName("basket provides discounted value when containing...")
    @MethodSource("routingData")
    @ParameterizedTest(name = "{0} {1} ({2}) -> {3}, ({4})")
    public void testRouting(String method, String path, String inputBody, int statusCode, String outputBody) {
        var servlet = new RoutingServlet();
        var result = servlet.route(method, path, () -> inputBody);
        assertEquals(statusCode, result.status);
        assertEquals(outputBody, result.body);
    }

    static Stream<Arguments> routingData() {
        return Stream.of(
                request("GET", "/", "", 200, null),
                request("GET", "/events/johan", "", 200, "{\"employee\": \"johan\", \"events\": []}"),
                request("POST", "/events/johan", "{\"date\": \"2020-12-10\"}", 201, null),
                request("GET", "/a/path/that/does/not/exist", "", 404, null)
        );
    }

    private static Arguments request(String method, String path, String requestBody, int statusCode, String responseBody) {
        return arguments(method, path, requestBody, statusCode, responseBody);
    }
}
