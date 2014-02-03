package org.robins.io.pubsub.domain;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * ***********************************************************************
 * Author: Jonathon Robins <jonathon.robins@pressassociation.com>        *
 * Created: 28/01/14 09:04                                               *
 * ***********************************************************************
 */
@XmlRootElement
public class Notification
{
    private String user;
    private String topic;
    private String message;

    public Notification(){}

    public Notification(String user, String topic, String message)
    {
        this.user = user;
        this.topic = topic;
        this.message = message;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
{
    this.user = user;
}

    public String getTopic()
    {
        return topic;
    }

    public void setTopic(String topic)
    {
        this.topic = topic;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}

