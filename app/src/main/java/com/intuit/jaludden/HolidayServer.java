package com.intuit.jaludden;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

import java.net.BindException;
import java.util.function.Function;

public class HolidayServer {

    private final Function<Integer, ServerWrapper> serverCreator;
    private ServerWrapper jettyServer;

    public HolidayServer() {
        this((port) -> new JettyWrapper(new Server(port)));
    }

    public static HolidayServer createNull() {
        return new HolidayServer((port) -> new NullServer());
    }

    private HolidayServer(Function<Integer, ServerWrapper> serverCreator) {
        this.serverCreator = serverCreator;
    }

    public void startServer(int port) {
        if (isStarted()) {
            throw new RuntimeException("can't start server because it's already running");
        }
        try {
            jettyServer = serverCreator.apply(port);
            jettyServer.createConnector(port);

            var handler = new ServletHandler();
            jettyServer.setHandler(handler);
            handler.addServletWithMapping(RoutingServlet.class, "/*");

            jettyServer.start();
        } catch (BindException e) {
            throw new RuntimeException("Can't start server due to error (" + e.getMessage() + ")", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stopServer() throws Exception {
        if(jettyServer == null) {
            throw new RuntimeException("can't stop server because it's not started");
        }
        jettyServer.stop();
        jettyServer = null;
    }

    public boolean isStarted() {
        return jettyServer != null;
    }

    private interface ServerWrapper {
        void start() throws Exception;
        void stop() throws Exception;
        void createConnector(int port);
        void setHandler(ServletHandler handler);
    }

    private static class NullServer implements ServerWrapper {
        @Override public void start() {}
        @Override public void stop() {}
        @Override public void createConnector(int port) {}
        @Override public void setHandler(ServletHandler handler) {}
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

        @Override
        public void createConnector(int port) {
            var c = new ServerConnector(s);
            c.setPort(port);
            s.setConnectors(new Connector[]{c});
        }
    }
}
