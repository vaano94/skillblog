package com.kravchenko.receiver;

import com.kravchenko.domain.Article;
import com.kravchenko.domain.ArticleCriteria;
import com.kravchenko.service.ArticleService;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.ArrayList;

/**
 * Created by john on 4/27/17.
 */
@Component
public class ArticleJmsResolver implements MessageListener {
    private static int ackMode;
    private static String messageBrokerUrl;
    private static String messageQueueName;
    private static String serverQueueName;
    private static String ARTICLE_PAGE_RQ;
    private static String ARTICLE_PAGE_RS;
    private static String ARTICLE_ID_RQ;
    private static String ARTICLE_ID_RS;

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

    @Autowired
    ArticleService articleService;

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

    @SuppressWarnings("Duplicates")
    @Override
    @JmsListener(destination = "client.messages")
    public void onMessage(Message mes) {
            ObjectMessage message = (ObjectMessage) mes;
            try {
                String messageID = message.getStringProperty("serviceCode");
                switch (messageID) {
                    case "ARTICLE_SAVE": {
                        jmsTemplate.send(serverQueueName, session -> {
                            TextMessage txtMsg = new ActiveMQTextMessage();
                            txtMsg.setText("HELLO WORLD");
                            txtMsg.setJMSCorrelationID(message.getJMSCorrelationID());
                            return txtMsg;
                        });
                        break;
                    }
                    case "ARTICLE_UPDATE": {
                        ObjectMessage objectMessage = (ObjectMessage) message;
                        articleService.saveOrUpdateArticle((Article) objectMessage.getObject());
                        break;
                    }
                    case "ARTICLE_DELETE": {
                        long articleId = message.getLongProperty("articleId");
                        articleService.deleteArticle(articleId);
                        break;
                    }
                    case "ARTICLE_FIND_ID": {
                        long articleId = message.getLongProperty("articleId");
                        Article article = articleService.getArticleById(articleId);
                        jmsTemplate.send(serverQueueName, session -> {
                            ObjectMessage responseMessage = new ActiveMQObjectMessage();
                            responseMessage.setJMSCorrelationID(message.getJMSCorrelationID());
                            responseMessage.setObject(article);
                            return responseMessage;
                        });
                        break;
                    }
                    case "ARTICLE_FIND_PAGE": {
                        ObjectMessage objectMessage = (ObjectMessage) message;
                        ArticleCriteria articleCriteria = (ArticleCriteria) objectMessage.getObject();
                        ArrayList<Article> articles = (ArrayList<Article>) articleService.getArticlePerPage(articleCriteria.getTag(), articleCriteria.getOffset(), articleCriteria.getPageSize());
                        jmsTemplate.send(serverQueueName, session -> {
                            ObjectMessage responseMessage = new ActiveMQObjectMessage();
                            responseMessage.setJMSCorrelationID(message.getJMSCorrelationID());
                            responseMessage.setObject(articles);
                            return responseMessage;
                        });
                        break;
                    }
                }
            } catch (JMSException e) {
            e.printStackTrace();
            }
        }
}