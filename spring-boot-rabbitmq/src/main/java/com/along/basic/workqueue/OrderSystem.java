package com.along.basic.workqueue;

import com.alibaba.fastjson2.JSON;
import com.along.utils.RabbitUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.along.utils.RabbitConstant.QUEUE_SMS;

/**
 * @Description: 发送者
 * @Author along
 * @Date 2023/12/4 22:11
 */
public class OrderSystem {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_SMS, false, false, false, null);

        for (int i = 0; i < 100; i++) {
            SMS sms = new SMS("乘客" + i, "1390000000" + i, "您的车票已订购成功");
            String jsonSMS = JSON.toJSONString(sms);
            channel.basicPublish("", QUEUE_SMS, null, jsonSMS.getBytes());

        }
        System.out.println("=========发送成功=======");
        channel.close();
        connection.close();

    }
}
