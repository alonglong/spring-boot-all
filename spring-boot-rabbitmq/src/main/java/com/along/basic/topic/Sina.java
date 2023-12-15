package com.along.basic.topic;

import com.along.utils.RabbitUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

import static com.along.utils.RabbitConstant.EXCHANGE_WEATHER;
import static com.along.utils.RabbitConstant.QUEUE_SINA;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author along
 * @Date 2023/12/5 22:45
 */
public class Sina {
    public static void main(String[] args) throws IOException {
        // 获取TCP长连接
        Connection connection = RabbitUtils.getConnection();
        // 获取虚拟连接
        Channel channel = connection.createChannel();
        // 声明队列信息
        channel.queueDeclare(QUEUE_SINA, false, false, false, null);
        // 队列绑定交换机
        // 参数1:队列名称
        // 参数2:交换机名称
        // 参数3:路由key，支持通配符
        channel.queueBind(QUEUE_SINA, EXCHANGE_WEATHER, "*.*.*.*");

        channel.basicQos(1); // 消费完一条再消费下一条
        channel.basicConsume(QUEUE_SINA, false, new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("新浪天气收到气象信息:" + new String(body));
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }
}
