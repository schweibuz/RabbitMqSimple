package com.reader;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.stuff.Local;
import com.stuff.RabbitConnection;


public class LocalReader extends Local implements RabbitConnection {
    
    private ConnectionFactory factory;

    public LocalReader() {
        createRabbitConnection(getExchangeName(), getHOST());
    }

    @Override
    public void createRabbitConnection(String exchangeName, String host) {
        factory = new ConnectionFactory();
        factory.setHost(host);

        try {
            final Connection connection = factory.newConnection();
            final Channel channel = connection.createChannel();

            channel.exchangeDeclare(exchangeName, "fanout");
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, getExchangeName(), "");

            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");

                System.out.println(" [x] Received '" + message + "'");
                System.out.println(" [x] Done");
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
