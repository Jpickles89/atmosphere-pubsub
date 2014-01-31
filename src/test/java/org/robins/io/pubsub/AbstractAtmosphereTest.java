package org.robins.io.pubsub;

import org.atmosphere.container.JettyAsyncSupportWithWebSocket;
import org.atmosphere.cpr.AtmosphereServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * ***********************************************************************
 * Author: Jonathon Robins <jonathon.robins@pressassociation.com>        *
 * Created: 29/01/14 12:39                                               *
 * ***********************************************************************
 */
public abstract class AbstractAtmosphereTest
{
    protected static final Logger logger = LoggerFactory.getLogger(AbstractAtmosphereTest.class);

    protected static int PORT;
    protected static String HOST;

    /* Unused */
    protected AtmosphereServlet atmosphereServlet;
    private Server server;

    /**
     *
     * Currently the Jetty Servcer does not working with websockets, so run tests as integration tests.
     *
     */
    @Deprecated
    public static void setUpProperties() throws Exception
    {
        Properties properties = new Properties();
        properties.load(AbstractAtmosphereTest.class.getResourceAsStream("jetty.properties"));

        HOST = properties.getProperty("host");
        PORT = Integer.parseInt(properties.getProperty("port"));
    }

    /**
     *
     * Currently the Jetty Servcer does not working with websockets, so run tests as integration tests.
     *
     */
    @Deprecated
    public void setUpGlobal() throws Exception
    {
        atmosphereServlet = new AtmosphereServlet();
        atmosphereServlet.framework().addInitParameter("com.sun.jersey.config.property.packages", "org.robins.io.pubsub");
        atmosphereServlet.framework().addInitParameter("org.atmosphere.websocket.messageContentType", "application/json");
        atmosphereServlet.framework().addInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
        atmosphereServlet.framework().setAsyncSupport(new JettyAsyncSupportWithWebSocket(atmosphereServlet.framework().getAtmosphereConfig()));

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(new ServletHolder(atmosphereServlet), "/api/*");

        server = new Server(PORT);
        server.setHandler(context);
        server.start();
        server.join();
    }

    /**
     *
     * Currently the Jetty Servcer does not working with websockets, so run tests as integration tests.
     *
     */
    @Deprecated
    public void tearDownGlobal() throws Exception
    {
        atmosphereServlet.destroy();
        server.stop();
    }

    /**
     * Build Request URL
     *
     * @param request
     * @return
     */
    public String requestUrl(String request)
    {
        return "http://" + HOST + ":" + PORT + request;
    }
}
