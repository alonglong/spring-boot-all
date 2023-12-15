package com.along.basic.helloword;

import com.along.utils.RabbitUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.along.utils.RabbitConstant.QUEUE_HELLOWORD;

public class Producer {

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

        String message = "hello rabbitmq";
        // 发送消息
        // 参数说明
        // exchange 交换机
        // routingKey 队列名称
        // basicProperties 消息属性
        // body 消息内容
        channel.basicPublish("", QUEUE_HELLOWORD, null, message.getBytes());
        channel.close();
        connection.close();
        System.out.println("=========发送成功=======");


    }
}
