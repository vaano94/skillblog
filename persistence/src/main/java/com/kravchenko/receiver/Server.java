package com.kravchenko.receiver;

import com.kravchenko.config.JmsConfig;
import com.kravchenko.domain.Article;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.*;

/**
 * Created by john on 4/27/17.
 */
@Component
public class Server implements MessageListener {
    private static int ackMode;
    private static String messageBrokerUrl;
    private static String messageQueueName;
    private static String serverQueueName;

    private Session session;
    private boolean transacted = false;
    private MessageProducer replyProducer;

    @Autowired
    private ActiveMQConnectionFactory connectionFactory;

    static {
        messageBrokerUrl = "tcp://localhost:61616";
        messageQueueName = "client.messages";
        serverQueueName = "server.messages";
        ackMode = Session.AUTO_ACKNOWLEDGE;
    }

    @Autowired
    JmsTemplate jmsTemplate;

//    @PostConstruct
//    private void setupMessageQueueConsumer() {
//        Connection connection;
//        try {
//            if (connectionFactory == null) {
//                connectionFactory = new JmsConfig().connectionFactory();
//            }
//            connection = connectionFactory.createConnection();
//            connection.start();
//            this.session = connection.createSession(this.transacted, ackMode);
//            Destination adminQueue = this.session.createQueue(messageQueueName);
//
//            //Setup a message producer to respond to messages from clients, we will get the destination
//            //to send to from the JMSReplyTo header field from a Message
//            this.replyProducer = this.session.createProducer(null);
//            this.replyProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
//
//            //Set up a consumer to consume messages off of the admin queue
//            MessageConsumer consumer = this.session.createConsumer(adminQueue);
//            consumer.setMessageListener(this);
//        } catch (JMSException e) {
//            //Handle the exception appropriately
//        }
//    }

    @Override
    @JmsListener(destination = "client.messages")
    public void onMessage(Message message) {
            jmsTemplate.send(serverQueueName, session -> {
                    TextMessage txtMsg = new ActiveMQTextMessage();
                    txtMsg.setText("HEELLO WORLD");
                    txtMsg.setJMSCorrelationID(message.getJMSCorrelationID());
                    return txtMsg;
            });
        }
}