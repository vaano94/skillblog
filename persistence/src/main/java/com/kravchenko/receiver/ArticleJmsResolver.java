package com.kravchenko.receiver;

import com.kravchenko.domain.Article;
import com.kravchenko.domain.ArticleCriteria;
import com.kravchenko.service.ArticleService;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import com.kravchenko.util.ArticleActions;

import javax.jms.*;
import java.util.ArrayList;

@Component
public class ArticleJmsResolver implements MessageListener {
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
    private JmsTemplate jmsTemplate;

    @Autowired
    private ArticleService articleService;

    @SuppressWarnings("Duplicates")
    @Override
    @JmsListener(destination = "client.messages")
    public void onMessage(Message mes) {
            ObjectMessage message = (ObjectMessage) mes;
            try {
                String messageID = message.getStringProperty("serviceCode");
                switch (messageID) {
                    case ArticleActions.SAVE: {
                        jmsTemplate.send(serverQueueName, session -> {
                            TextMessage txtMsg = new ActiveMQTextMessage();
                            txtMsg.setText("HELLO WORLD");
                            txtMsg.setJMSCorrelationID(message.getJMSCorrelationID());
                            return txtMsg;
                        });
                        break;
                    }
                    case ArticleActions.UPDATE: {
                        ObjectMessage objectMessage = (ObjectMessage) message;
                        articleService.saveOrUpdateArticle((Article) objectMessage.getObject());
                        break;
                    }
                    case ArticleActions.DELETE: {
                        Long articleId = (Long) message.getObject();
                        boolean deleted = articleService.deleteArticle(articleId);
                        jmsTemplate.send(serverQueueName, session -> {
                            ObjectMessage responseMessage = new ActiveMQObjectMessage();
                            responseMessage.setJMSCorrelationID(message.getJMSCorrelationID());
                            responseMessage.setObject(deleted);
                            return responseMessage;
                        });
                        break;
                    }
                    case ArticleActions.FIND_ID: {
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
                    case ArticleActions.FIND_PAGE: {
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