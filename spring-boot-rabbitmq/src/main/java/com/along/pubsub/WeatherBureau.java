package com.along.pubsub;

import com.along.utils.RabbitUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import static com.along.utils.RabbitConstant.EXCHANGE_WEATHER;

/**
 * @Description: 发布订阅模式
 * @Author along
 * @Date 2023/12/5 22:36
 */
public class WeatherBureau {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitUtils.getConnection();
        // 获取键盘输入的信息
        String next = new Scanner(System.in).next();
        Channel channel = connection.createChannel();
        // 声明交换机
        // 参数1:交换机名称
        // 参数2:交换机类型
        // 参数3:是否持久化
        // 参数4:是否自动删除
        channel.basicPublish(EXCHANGE_WEATHER, "", null, next.getBytes());

        channel.close();
        connection.close();
    }
}
