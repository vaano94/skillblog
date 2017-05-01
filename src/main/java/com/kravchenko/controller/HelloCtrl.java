package com.kravchenko.controller;

import com.kravchenko.domain.Article;
import com.kravchenko.receiver.Server;
import org.apache.activemq.Service;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.*;
import javax.jms.Message;
import java.util.Date;
import java.util.UUID;

/**
 * Created by john on 4/16/17.
 */
@RestController
public class HelloCtrl {

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    ActiveMQConnectionFactory connectionFactory;
    private static String messageBrokerUrl;
    private static String messageQueueName;
    private static String serverQueueName;

    static {
        messageBrokerUrl = "localhost:61616";
        messageQueueName = "client.messages";
        serverQueueName = "server.messages";
    }

    @GetMapping(value = "/show/{id}")
    public Article hello(@PathVariable Integer id) {

        Article article = new Article();
        article.setAuthor("Ivan");
        article.setContent(String.valueOf(id));
        article.setBeginDate(new Date());

        try {
            MessageProducer producer;


            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            Destination adminQueue = session.createQueue(messageQueueName);

            //Setup a message producer to send message to the queue the server is consuming from
            producer = session.createProducer(adminQueue);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            //Create a temporary queue that this client will listen for responses on then create a consumer
            //that consumes message from this temporary queue...for a real application a client should reuse
            //the same temp queue for each message to the server...one temp queue per client
            Destination tempDest = session.createTemporaryQueue();
            MessageConsumer responseConsumer = session.createConsumer(tempDest);

//            responseConsumer.setMessageListener(this);

            ObjectMessage message = session.createObjectMessage(article);

            message.setJMSReplyTo(tempDest);
            //message.setJMSCorrelationID(UUID.randomUUID().toString());
            //producer.send(message);
            
            jmsTemplate.send(messageQueueName, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    Destination tempDest = session.createTemporaryQueue();
                    MessageConsumer responseConsumer = session.createConsumer(tempDest);

//                    responseConsumer.setMessageListener();

                    ObjectMessage message = session.createObjectMessage(article);
                    message.setJMSCorrelationID(UUID.randomUUID().toString());
                    System.out.println(" I SENT " + message.getJMSCorrelationID());
                    return message;
                }
            });

            Message receive = jmsTemplate.receive(serverQueueName);
            System.out.println("RECEIVED SMTH " + receive.getJMSCorrelationID());
            System.out.println(receive.toString());


        } catch (JMSException e) {
            e.printStackTrace();
        }

        return article;
    }

//    @Override
//    public void onMessage(Message message) {
//        System.out.println(message.toString());
//    }
}
