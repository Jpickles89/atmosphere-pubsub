package org.robins.io.pubsub;

import org.atmosphere.wasync.Decoder;
import org.atmosphere.wasync.Event;
import org.codehaus.jackson.map.ObjectMapper;
import org.robins.io.pubsub.domain.Notification;

import java.io.IOException;

/**
 * ***********************************************************************
 * Author: Jonathon Robins <jonathon.robins@pressassociation.com>        *
 * Created: 30/01/14 12:07                                               *
 * ***********************************************************************
 */
public class JacksonDecoder implements Decoder<String, Notification>
{
    protected final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Notification decode(Event type, String data)
    {
        data = data.trim();

        if (data.length() == 0)
        {
            return null;
        }

        if (type.equals(Event.MESSAGE))
        {
            try
            {
                return mapper.readValue(data, Notification.class);
            }
            catch (IOException e)
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }
}
