package com.kravchenko.util;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.io.Serializable;
import java.util.UUID;

/**
 * Created by john on 8/4/17.
 */
@Component
public class JmsMessageWrapper<T extends Serializable> {


    public ObjectMessage createMessage(ObjectMessage message, T object) {
        ObjectMessage responseMessage = new ActiveMQObjectMessage();

        try {
            responseMessage.setJMSCorrelationID(message.getJMSCorrelationID());
            responseMessage.setObject(object);
        } catch (JMSException e) {
            e.printStackTrace();
        }

        return responseMessage;
    }

    public ObjectMessage createMessageWithSession(Session session, T object, String messageID, String serviceCode) {
        try {
            ObjectMessage msg = session.createObjectMessage(object);
            msg.setJMSMessageID(ArticleActions.DELETE);
            msg.setStringProperty("serviceCode", ArticleActions.DELETE);
            msg.setJMSCorrelationID(UUID.randomUUID().toString());
            return msg;
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }

}
