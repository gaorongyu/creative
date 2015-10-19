package com.fngry.core.jms.rabbitmq;

import com.rabbitmq.client.*;

/**
 * Created by gaorongyu on 15/9/15.
 */
public class Consumer {

    private final static String QUEUE_NAME = "gaorongyu_test";

    public static void main(String[] argv) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("121.40.214.164");
        factory.setVirtualHost("creative");
        factory.setPort(5672);
        factory.setUsername("gaorongyu");
        factory.setPassword("gry@00168");

        Connection connection = null;
        try {
            connection = factory.newConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        final Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        channel.basicQos(1);

        QueueingConsumer consumer = new QueueingConsumer(channel);

//        QueueingConsumer consumer = new QueueingConsumer(channel) {
//            @Override
//            public void handleDelivery(String consumerTag,
//                                                   Envelope envelope,
//                                                   AMQP.BasicProperties properties,
//                                                   byte[] body) {
//                String message = new String(body);
//                System.out.println(" [x] Received '" + message + "'");
//                try {
//                    Thread.sleep(2000);
//                    channel.basicAck(envelope.getDeliveryTag(), false);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
        channel.basicConsume(QUEUE_NAME, true, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println(" [x] Received '" + message + "'");

            if(message != null && message.length() > 0) {

                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }
            }
        }
    }

}
