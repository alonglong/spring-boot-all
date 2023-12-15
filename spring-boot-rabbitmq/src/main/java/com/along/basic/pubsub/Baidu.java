package com.along.basic.pubsub;

import com.along.utils.RabbitUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

import static com.along.utils.RabbitConstant.*;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author along
 * @Date 2023/12/5 22:45
 */
public class Baidu {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        // 声明队列信息
        channel.queueDeclare(QUEUE_BAIDU, false, false, false, null);
        // 队列绑定交换机
        // 参数1:队列名称
        // 参数2:交换机名称
        // 参数3:路由key
        channel.queueBind(QUEUE_BAIDU, QUEUE_HELLOWORD, "");
        channel.basicQos(1);
        channel.basicConsume(QUEUE_BAIDU, false, new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("新浪天气收到气象信息:" + new String(body));
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }
}
