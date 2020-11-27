package com.intuit.jaludden;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.net.BindException;
import java.util.function.Function;

public class HolidayServer {

    private final Function<Integer, ServerWrapper> serverCreator;
    private final RoutingServlet routingServlet;
    private ServerWrapper server;

    public HolidayServer(RoutingServlet routingServlet) {
        this((port) -> new JettyWrapper(new Server(port)), routingServlet);
    }

    public static HolidayServer createNull() {
        return new HolidayServer((port) -> new NullServer(), new RoutingServlet(new EventController(new DatabaseEventRepository(HolidayDatabase.createNull())), new DirectReportsController()));
    }

    private HolidayServer(Function<Integer, ServerWrapper> serverCreator, RoutingServlet routingServlet) {
        this.serverCreator = serverCreator;
        this.routingServlet = routingServlet;
    }

    public void startServer(int port) {
        if (isStarted()) {
            throw new RuntimeException("can't start server because it's already running");
        }
        try {
            server = serverCreator.apply(port);

            var handler = new ServletHandler();
            handler.addServletWithMapping(new ServletHolder(routingServlet), "/*");
            server.setHandler(handler);

            server.start();
        } catch (BindException e) {
            throw new RuntimeException("Can't start server due to error (" + e.getMessage() + ")", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stopServer() throws Exception {
        if (server == null) {
            throw new RuntimeException("can't stop server because it's not started");
        }
        server.stop();
        server = null;
    }

    public boolean isStarted() {
        return server != null;
    }

    private interface ServerWrapper {
        void start() throws Exception;

        void stop() throws Exception;

        void setHandler(ServletHandler handler);
    }

    private static class JettyWrapper implements ServerWrapper {
        private final Server s;

        public JettyWrapper(Server s) {
            this.s = s;
        }

        @Override
        public void setHandler(ServletHandler handler) {
            s.setHandler(handler);
        }

        @Override
        public void start() throws Exception {
            s.start();
        }

        @Override
        public void stop() throws Exception {
            s.stop();
        }
    }

    private static class NullServer implements ServerWrapper {
        @Override
        public void start() {
        }

        @Override
        public void stop() {
        }

        @Override
        public void setHandler(ServletHandler handler) {
        }
    }
}
