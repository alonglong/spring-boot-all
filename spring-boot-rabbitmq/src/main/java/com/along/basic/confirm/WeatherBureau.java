package com.along.basic.confirm;

import com.along.utils.RabbitUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static com.along.utils.RabbitConstant.EXCHANGE_WEATHER_TOPIC;

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

        //开启confirm监听模式
        channel.confirmSelect();
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long l, boolean b) throws IOException {
                // 第二个参数代表接收的数据是否为批量接收，一般用不到
                System.out.println("消息已经被Broker接收，Tag:" + l);
            }

            @Override
            public void handleNack(long l, boolean b) throws IOException {
                System.out.println("消息已经被Broker拒收，Tag:" + l);
            }
        });

        channel.addReturnListener(new ReturnCallback() {
            @Override
            public void handle(Return aReturn) {
                System.out.println("========================");
                System.out.println("Return编码：" + aReturn.getReplyCode() + "-Return描述:" + aReturn.getReplyText());
                System.out.println("交换机：" + aReturn.getExchange() + "-路由key:" + aReturn.getRoutingKey());
                System.out.println("return主题：" + new String(aReturn.getBody()));
                System.out.println("========================");
            }
        });



        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            // 声明交换机
            channel.basicPublish(EXCHANGE_WEATHER_TOPIC, next.getKey(), null, next.getValue().getBytes());
        }

//        channel.close();
//        connection.close();
    }
}
