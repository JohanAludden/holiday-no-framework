package com.intuit.jaludden;

import com.intuit.jaludden.directreport.DirectReportsController;
import com.intuit.jaludden.event.DatabaseEventRepository;
import com.intuit.jaludden.event.EventController;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HolidayServerTest {

    public static final int PORT = 8080;

    @Test
    public void testStartAndStopServer() throws Exception {
        var server = new HolidayServer(new RoutingServlet(new EventController(new DatabaseEventRepository(HolidayDatabase.createNull())), new DirectReportsController()));
        server.startServer(PORT);
        server.stopServer();

        server.startServer(PORT);
        server.stopServer();
    }

    @Test
    public void testKnowsRunningState() throws Exception {
        var server = new HolidayServer(new RoutingServlet(new EventController(new DatabaseEventRepository(HolidayDatabase.createNull())), new DirectReportsController()));
        assertFalse(server.isStarted());
        startAndStopServer(server, ignored -> assertTrue(server.isStarted()));
        assertFalse(server.isStarted());
    }

    @Test
    public void failsGracefullyOnStartupErrors() throws Exception {
        startAndStopServer(s -> {
            var server2 = new HolidayServer(new RoutingServlet(new EventController(new DatabaseEventRepository(HolidayDatabase.createNull())), new DirectReportsController()));
            RuntimeException e = assertThrows(RuntimeException.class, () -> server2.startServer(PORT));
            assertEquals("Can't start server due to error (Address already in use)", e.getMessage());
        });
    }

    @Test
    public void failsFastIfServerIsStartedTwice() throws Exception {
        startAndStopServer(s -> {
            RuntimeException e = assertThrows(RuntimeException.class, () -> s.startServer(PORT));
            assertEquals("can't start server because it's already running", e.getMessage());
        });
    }

    @Test
    public void failsFastIfServerIsStoppedBeforeStarted() {
        var server = new HolidayServer(new RoutingServlet(new EventController(new DatabaseEventRepository(HolidayDatabase.createNull())), new DirectReportsController()));
        RuntimeException e = assertThrows(RuntimeException.class, server::stopServer);
        assertEquals("can't stop server because it's not started", e.getMessage());
    }

    @Test
    public void testNullability() throws Exception {
        var server = HolidayServer.createNull();
        var server2 = HolidayServer.createNull();
        server.startServer(8080);
        server2.startServer(8080);
    }

    @Test
    public void realServerRespondsToRootPath() throws Exception {
        startAndStopServer(server -> {
            HttpResponse<String> response = getRequestFrom("/");
            assertEquals(200, response.statusCode());
        });
    }

    private HttpResponse<String> getRequestFrom(String path) throws java.io.IOException, InterruptedException {
        var client = HttpClient.newHttpClient();

        var request = HttpRequest.newBuilder(
                URI.create("http://localhost:8080" + path))
                .header("accept", "application/json")
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }


    private void startAndStopServer(ConsumerWithException<HolidayServer> whileStarted) throws Exception {
        var server = new HolidayServer(new RoutingServlet(new EventController(new DatabaseEventRepository(HolidayDatabase.createNull())), new DirectReportsController()));
        startAndStopServer(server, whileStarted);
    }

    private void startAndStopServer(HolidayServer server, ConsumerWithException<HolidayServer> whileStarted) throws Exception {
        server.startServer(PORT);
        try {
            whileStarted.accept(server);
        } finally {
            server.stopServer();
        }
    }

    private interface ConsumerWithException<T> {
        void accept(T t) throws Exception;
    }
}
