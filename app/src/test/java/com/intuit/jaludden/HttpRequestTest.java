package com.intuit.jaludden;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpRequestTest {

    private App app;

    @BeforeEach
    public void startServer() {
        app = new App();
        new Thread(() -> {
            try {
                app.serve();
            } catch (Exception e){}
        }).start();
    }

    @AfterEach
    public void stopServer() throws Exception {
        app.stop();
    }

    @Test
    public void respondsOnRoot() throws Exception{
        HttpResponse<String> response = sendRequestTo("/");
        assertEquals(response.statusCode(), 200);
    }

    @Test
    public void listEventsHaveJsonFormat() throws Exception {
        HttpResponse<String> response = sendRequestTo("/events/Johan");
        assertEquals(response.headers().firstValue("content-type").get(), "application/json");
    }

    @Test
    public void listEventsResponseBodyContainsEmployeeName() throws Exception {
        HttpResponse<String> response = sendRequestTo("/events/Johan");
        assertTrue(response.body().contains("Johan"));
    }

    @Test
    public void haveListEventForEmployeeEndpoint() throws Exception {
        HttpResponse<String> response = sendRequestTo("/events/Johan");
        assertEquals(response.statusCode(), 200);
    }

    @Test
    public void haveListEventHaveEmployeeNameInPath() throws Exception {
        HttpResponse<String> response = sendRequestTo("/events/Varsha");
        assertEquals(response.statusCode(), 200);
    }

    @Test
    public void respondsWithNotFoundForNotManagedPath() throws Exception {
        HttpResponse<String> response = sendRequestTo("/path/that/does/not/exist");
        assertEquals(response.statusCode(), 404);
    }

    private HttpResponse<String> sendRequestTo(String path) throws java.io.IOException, InterruptedException {
        var client = HttpClient.newHttpClient();

        var request = HttpRequest.newBuilder(
                URI.create("http://localhost:8080" + path))
                .header("accept", "application/json")
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

}
