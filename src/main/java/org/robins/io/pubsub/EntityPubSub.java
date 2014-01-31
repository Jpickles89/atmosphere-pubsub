package org.robins.io.pubsub;

import org.atmosphere.annotation.Broadcast;
import org.atmosphere.annotation.Suspend;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.jersey.Broadcastable;
import org.robins.io.pubsub.domain.Notification;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/entity/{topic}")
@Produces(MediaType.APPLICATION_JSON)
public class EntityPubSub
{
    @PathParam("topic")
    private Broadcaster topic;

    @GET
    @Suspend(listeners = {EventsLogger.class})
    public Broadcastable subscribe()
    {
        return new Broadcastable(topic);
    }

    @POST
    @Broadcast(writeEntity = false)
    public Broadcastable publish(Notification notification)
    {
        notification.setTopic(topic.getID());

        return new Broadcastable(notification, topic);
    }
}
