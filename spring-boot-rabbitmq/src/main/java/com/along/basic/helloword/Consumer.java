package com.along.basic.helloword;

import com.along.utils.RabbitUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.along.utils.RabbitConstant.QUEUE_HELLOWORD;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author along
 * @Date 2023/12/4 21:37
 */
public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取tcp长连接
        Connection connection = RabbitUtils.getConnection();
        // 创建channel
        Channel channel = connection.createChannel();

        // 创建队列，声明并创建队列，如果队列已经存在，则使用这个队列
        // 参数说明
        // queue 队列名称
        // durable 是否持久化 false 不持久化, mq重启之后信息会被丢弃 true 持久化
        // exclusive 是否独占
        // autoDelete 是否自动删除
        // arguments 队列参数
        channel.queueDeclare(QUEUE_HELLOWORD, false, false, false, null);

        // 从mq服务器接收消息
        // 参数说明
        // queue 队列名称
        // autoAck 是否自动确认, true 自动确认, false 手动确认(推荐)
        // consumerTag 消费者标签
        channel.basicConsume(QUEUE_HELLOWORD, false, new Receiver(channel));


    }
}

class Receiver extends DefaultConsumer {

    private Channel channel;

    public Receiver(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = new String(body);
        System.out.println("接收到消息:" + message);
        System.out.println("消息的tagId:" + envelope.getDeliveryTag());
        // false 只确认签收当前的消息
        channel.basicAck(envelope.getDeliveryTag(), false);
    }

}