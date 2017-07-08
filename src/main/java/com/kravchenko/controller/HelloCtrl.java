package com.kravchenko.controller;

import com.kravchenko.domain.Article;
import com.kravchenko.domain.ArticleCriteria;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.*;
import javax.jms.Message;
import java.util.Date;
import java.util.List;
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
            
            jmsTemplate.send(messageQueueName, innerSession -> {
                Destination tempDest1 = innerSession.createTemporaryQueue();
                MessageConsumer responseConsumer1 = innerSession.createConsumer(tempDest1);

//                    responseConsumer.setMessageListener();

                ObjectMessage message1 = innerSession.createObjectMessage(article);
                message1.setJMSMessageID("ARTICLE_FIND_PAGE");
                message1.setStringProperty("serviceCode", "ARTICLE_FIND_PAGE");
                message1.setJMSCorrelationID(UUID.randomUUID().toString());
                System.out.println(" I SENT " + message1.getJMSCorrelationID());
                return message1;
            });

            Message receive = jmsTemplate.receive(serverQueueName);
            System.out.println("RECEIVED SMTH " + receive.getJMSCorrelationID());
            System.out.println(receive.toString());


        } catch (JMSException e) {
            e.printStackTrace();
        }

        return article;
    }

    @GetMapping(value = "/page/{id}")
    public List<Article> getArticlePage(@PathVariable Integer id) {
        List<Article> articles = null;
        try {
            MessageProducer producer;

            jmsTemplate.send(messageQueueName, innerSession -> {
                Destination tempDest1 = innerSession.createTemporaryQueue();
                ArticleCriteria articleCriteria = new ArticleCriteria();
                articleCriteria.setOffset(0);
                articleCriteria.setPageSize(2);
                ObjectMessage message1 = innerSession.createObjectMessage(articleCriteria);
                message1.setJMSMessageID("ARTICLE_FIND_PAGE");
                message1.setStringProperty("serviceCode", "ARTICLE_FIND_PAGE");
                message1.setJMSCorrelationID(UUID.randomUUID().toString());
                System.out.println(" I SENT " + message1.getJMSCorrelationID());
                return message1;
            });

            Message receive = jmsTemplate.receive(serverQueueName);
            articles = (List<Article>) ((ActiveMQObjectMessage) receive).getObject();
            System.out.println(articles.toString());
            System.out.println("RECEIVED SMTH " + receive.getJMSCorrelationID());
            System.out.println(articles.toString());


        } catch (JMSException e) {
            e.printStackTrace();
        }

        return articles;
    }

//    @Override
//    public void onMessage(Message message) {
//        System.out.println(message.toString());
//    }
}
