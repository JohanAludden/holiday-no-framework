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
    public void startServer() throws Exception {
        app = new App(new HolidayServer());
        app.start();
    }

    @AfterEach
    public void stopServer() throws Exception {
        app.stop();
    }

    @Test
    public void respondsOnRoot() throws Exception{
        HttpResponse<String> response = getRequestFrom("/");
        assertEquals(200, response.statusCode());
    }

    @Test
    public void listEventsHaveJsonFormat() throws Exception {
        HttpResponse<String> response = getRequestFrom("/events/Johan");
        assertEquals("application/json", response.headers().firstValue("content-type").get());
    }

    @Test
    public void listEventsResponseBodyContainsEmployeeName() throws Exception {
        HttpResponse<String> response = getRequestFrom("/events/Johan");
        assertTrue(response.body().contains("Johan"));
    }

    @Test
    public void haveListEventForEmployeeEndpoint() throws Exception {
        HttpResponse<String> response = getRequestFrom("/events/Johan");
        assertEquals(200, response.statusCode());
    }

    @Test
    public void eventsSupportsPostAndGet() throws Exception {
        HttpResponse<String> postResponse = postRequestTo("/events/Johan", "{\"date\": \"2020-12-10\"}");
        assertEquals(201, postResponse.statusCode());
        HttpResponse<String> response = getRequestFrom("/events/Johan");
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Johan"), response.body());
        assertTrue(response.body().contains("2020-12-10"), response.body());
    }

    @Test
    public void haveListEventHaveEmployeeNameInPath() throws Exception {
        HttpResponse<String> response = getRequestFrom("/events/Varsha");
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Varsha"));
    }

    @Test
    public void respondsWithNotFoundForNotManagedPath() throws Exception {
        HttpResponse<String> response = getRequestFrom("/path/that/does/not/exist");
        assertEquals(404, response.statusCode());
    }

    private HttpResponse<String> getRequestFrom(String path) throws java.io.IOException, InterruptedException {
        var client = HttpClient.newHttpClient();

        var request = HttpRequest.newBuilder(
                URI.create("http://localhost:8080" + path))
                .header("accept", "application/json")
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> postRequestTo(String path, String body) throws java.io.IOException, InterruptedException {
        var client = HttpClient.newHttpClient();

        var request = HttpRequest.newBuilder(
                URI.create("http://localhost:8080" + path))
                .header("accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

}
