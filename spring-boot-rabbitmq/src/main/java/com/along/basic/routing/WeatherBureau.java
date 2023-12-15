package com.along.basic.routing;

import com.along.utils.RabbitUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import static com.along.utils.RabbitConstant.EXCHANGE_WEATHER;
import static com.along.utils.RabbitConstant.EXCHANGE_WEATHER_ROUTING;

/**
 * @Description: 发布订阅模式
 * @Author along
 * @Date 2023/12/5 22:36
 */
public class WeatherBureau {
    public static void main(String[] args) throws IOException, TimeoutException {
        Map<String, String> map = new HashMap<>();
        map.put("11111", "aaaaaa");
        map.put("22222", "bbbbbb");
        map.put("33333", "cccccc");
        map.put("44444", "dddddd");
        map.put("55555", "eeeeee");
        map.put("66666", "ffffff");
        map.put("77777", "gggggg");
        map.put("88888", "hhhhhh");
        map.put("99999", "iiiiii");

        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();

        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            // 声明交换机
            channel.basicPublish(EXCHANGE_WEATHER_ROUTING, next.getKey(), null, next.getValue().getBytes());
        }

        channel.close();
        connection.close();
    }
}
