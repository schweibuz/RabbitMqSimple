package com.writer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.stuff.Local;
import com.stuff.RabbitConnection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class LocalWriter extends Local implements RabbitConnection {

    private ConnectionFactory factory;
    private static Collection collection;

    public LocalWriter() {
        createContent();
        createRabbitConnection(getExchangeName(), getHOST());
    }

    @Override
    public void createRabbitConnection(String exchangeName, String host) {
        factory = new ConnectionFactory();
        factory.setHost(host);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(exchangeName, "fanout");

            Iterator iterator = collection.iterator();
            String message;

            while (iterator.hasNext()) {
                message = (String) iterator.next();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException _ignored) {
                    Thread.currentThread().interrupt();
                }
                channel.basicPublish(getExchangeName(), "", null,
                        message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + message + "'");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createContent() {
        collection = new ArrayList();
        for (int i = 0; i < 10; i++) {
            collection.add("Message " + i);
        }
    }

}
