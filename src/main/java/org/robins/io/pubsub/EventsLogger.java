package org.robins.io.pubsub;

import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.websocket.WebSocketEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventsLogger implements WebSocketEventListener
{
    private static final Logger logger = LoggerFactory.getLogger(EventsLogger.class);

    public EventsLogger()
    {
    }

    @Override
    public void onPreSuspend(AtmosphereResourceEvent event)
    {
        logger.info("onPreSuspend(): {}", event.getMessage());
    }

    public void onSuspend(final AtmosphereResourceEvent event)
    {
        logger.info("onSuspend(): {}", event.getMessage());
    }

    public void onResume(AtmosphereResourceEvent event)
    {
        logger.info("onResume(): {}", event.getMessage());
    }

    public void onDisconnect(AtmosphereResourceEvent event)
    {
        logger.info("onDisconnect(): {}", event.getMessage());
    }

    public void onBroadcast(AtmosphereResourceEvent event)
    {
        logger.info("onBroadcast(): {}", event.getMessage());
    }

    public void onThrowable(AtmosphereResourceEvent event)
    {
        logger.warn("onThrowable(): {}", event);
    }

    @Override
    public void onClose(AtmosphereResourceEvent event)
    {
        logger.info("onClose(): {}", event.getMessage());
    }

    public void onHandshake(WebSocketEvent event)
    {
        logger.info("onHandshake(): {}", event);
    }

    public void onMessage(WebSocketEvent event)
    {
        logger.info("onMessage(): {}", event);
    }

    public void onClose(WebSocketEvent event)
    {
        logger.info("onClose(): {}", event);
    }

    public void onControl(WebSocketEvent event)
    {
        logger.info("onControl(): {}", event);
    }

    public void onDisconnect(WebSocketEvent event)
    {
        logger.info("onDisconnect(): {}", event);
    }

    public void onConnect(WebSocketEvent event)
    {
        logger.info("onConnect(): {}", event);
    }
}
