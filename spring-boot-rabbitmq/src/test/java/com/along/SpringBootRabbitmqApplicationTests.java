package com.along;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sound.midi.Soundbank;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootRabbitmqApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void send() {

        // 设置交换机处理失败消息的模式，为true时,消息到达不了queue时，会将消息返回给生产者
        rabbitTemplate.setMandatory(true);

        // 定义回调，confirm模式回调，消息到达不了Broker交换机的时候会调这个方法
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {

            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if (ack) {
                    System.out.println("消息发送成功");
                } else {
                    System.out.println("消息发送失败");
                    // 做一些处理，比如消息重发
                }
            }
        });

        // return模式回调，消息到达不了queue的时候会调这个方法
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                System.out.println("消息发送失败");
                System.out.println("message:" + message);
                System.out.println("replyCode:" + replyCode);
                System.out.println("replyText:" + replyText);
                System.out.println("exchange:" + exchange);
                System.out.println("routingKey:" + routingKey);

                // 处理
            }
        });

        // 发送消息
        rabbitTemplate.convertAndSend("boot_topic_exchange", "boot.#", "hello rabbitmq");

        // 进行睡眠操作
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    // 批量发送消息，让消费者每次拉取指定的数量
    @Test
    public void testQos() {
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("boot_topic_exchange", "boot.#", "hello rabbitmq" + i);
        }
    }

    @Test
    public void testTTL() {

    }

}
