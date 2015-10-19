package com.fngry.core.jms.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * Created by gaorongyu on 15/9/15.
 */
public class Producer {

    private final static String QUEUE_NAME = "gaorongyu_test";

    /**
     *
     * @param argv
     * @throws Exception
     */
    public static void main(String[] argv) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("121.40.214.164");
        factory.setVirtualHost("creative");
//        factory.setPort(15672);
        factory.setUsername("gaorongyu");
        factory.setPassword("gry@00168");

//            factory.setHost("112.124.66.38");
//    factory.setVirtualHost("tqmall");
////        factory.setPort(15672);
//    factory.setUsername("tqmall");
//    factory.setPassword("tqmalltest");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        for(int i = 0; i < 7; i++) {
            String message = "Hello World!@#$%" + i;
            channel.basicPublish("",
                    QUEUE_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }

        channel.close();
        connection.close();
    }

//    factory.setHost("112.124.66.38");
//    factory.setVirtualHost("tqmall");
////        factory.setPort(15672);
//    factory.setUsername("tqmall");
//    factory.setPassword("tqmalltest");

}
