package org.robins.io.pubsub;

import org.atmosphere.wasync.*;
import org.atmosphere.wasync.impl.AtmosphereClient;
import org.atmosphere.wasync.impl.AtmosphereRequest;
import org.junit.BeforeClass;
import org.junit.Test;
import org.robins.io.pubsub.domain.Notification;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * ***********************************************************************
 * Author: Jonathon Robins <jonathon.robins@pressassociation.com>        *
 * Created: 29/01/14 11:31                                               *
 * ***********************************************************************
 */
public class EntityPubSubTestIT extends AbstractAtmosphereTest
{
    @BeforeClass
    public static void setUpProperties() throws Exception
    {
        Properties properties = new Properties();
        properties.load(AbstractAtmosphereTest.class.getResourceAsStream("integration.properties"));

        HOST = properties.getProperty("host");
        PORT = Integer.parseInt(properties.getProperty("port"));
    }

    @Test
    public void testTopicSubscription() throws Exception
    {
        List<Notification> notifications1 = new ArrayList<Notification>();
        List<Notification> notifications2 = new ArrayList<Notification>();
        List<Notification> notifications3 = new ArrayList<Notification>();

        Socket socket1 = createSocket("test1", notifications1);
        Socket socket2 = createSocket("test1", notifications2);
        Socket socket3 = createSocket("test2", notifications3);

        socket1.fire(new Notification("Walter", "test1", "Message 1"));
        Thread.sleep(500);

        socket2.fire(new Notification("Saul", "test1", "Message 2"));
        Thread.sleep(500);

        socket3.fire(new Notification("Gus", "test2", "Message 3"));
        Thread.sleep(500);

        socket1.close();
        socket2.close();
        socket3.close();

        assertEquals(2, notifications1.size());
        assertEquals(2, notifications2.size());
        assertEquals(1, notifications3.size());
    }

    /**
     * Test Helper to create a connection to a given topic.
     *
     * @param topic
     * @return
     * @throws Exception
     */
    public Socket createSocket(String topic,final List<Notification> notifications) throws Exception
    {
        AtmosphereClient client = ClientFactory.getDefault().newClient(AtmosphereClient.class);

        AtmosphereRequest.AtmosphereRequestBuilder request = client.newRequestBuilder()
            .method(Request.METHOD.GET)
            .uri("http://"+HOST+":"+PORT+"/api/entity/" + topic)
            .trackMessageLength(true)
            .header("Content-Type", MediaType.APPLICATION_JSON)
            .header("Accept", MediaType.APPLICATION_JSON)
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
            .transport(Request.TRANSPORT.WEBSOCKET)
            .transport(Request.TRANSPORT.LONG_POLLING);

        Socket socket = client.create();

        socket.on(Event.MESSAGE.name(), new Function<Notification>() {
            @Override
            public void on(Notification t) {
                logger.info("Notification {}", t.getMessage());
                notifications.add(t);
            }
        }).on(Event.OPEN.name(), new Function<String>()
        {
            @Override
            public void on(String s)
            {
                logger.info("Opened {}", s);
            }
        }).on(Event.CLOSE.name(), new Function<String>()
        {
            @Override
            public void on(String s)
            {
                logger.info("Closed {}", s);
            }
        })
        .open(request.build());

        //Let the Socket Connect before creating a new one
        Thread.sleep(1000);
        return socket;
    }
}
