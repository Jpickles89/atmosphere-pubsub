package org.robins.io.pubsub;

import org.atmosphere.wasync.Encoder;
import org.codehaus.jackson.map.ObjectMapper;
import org.robins.io.pubsub.domain.Notification;

import java.io.IOException;

/**
 * ***********************************************************************
 * Author: Jonathon Robins <jonathon.robins@pressassociation.com>        *
 * Created: 30/01/14 12:07                                               *
 * ***********************************************************************
 */
public class JacksonEncoder implements Encoder<Notification, String>
{
    protected final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String encode(Notification notification)
    {
        try
        {
            return mapper.writeValueAsString(notification);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
