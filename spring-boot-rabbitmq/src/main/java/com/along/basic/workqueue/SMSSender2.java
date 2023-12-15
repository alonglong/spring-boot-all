package com.along.basic.workqueue;

import com.along.utils.RabbitUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

import static com.along.utils.RabbitConstant.QUEUE_SMS;

/**
 * @Description: 消费者
 * @Author along
 * @Date 2023/12/4 22:22
 */
public class SMSSender2 {

    public static void main(String[] args) throws IOException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_SMS, false, false, false, null);

        channel.basicQos(1); // 设置每次只接收一个消息，否则会阻塞
        channel.basicConsume(QUEUE_SMS, false, new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body);
                System.out.println("SMSSender2 接收到消息:" + message);
                System.out.println("消息的tagId:" + envelope.getDeliveryTag());
                System.out.println("发送消息。。。");
                // false 确认签收当前的消息
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });

    }
}
