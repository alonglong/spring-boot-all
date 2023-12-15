package com.along.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Description: rabbitmq消息监听器，消费者
 * @Author along
 * @Date 2023/12/15 10:28
 */
@Component
public class RabbitMqListener {

    // 定义方法进行监听
    @RabbitListener(queues = "boot_queue")
    public void listenerQueue(Message message, Channel channel) {
        try {
            // 获取消息的id
            long deliveryTag = message.getMessageProperties().getDeliveryTag();
            System.out.println("deliveryTag: " + deliveryTag);

            System.out.println("收到消息: " + new String(message.getBody()));

            // 手动确认消息
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            // 处理异常情况
            e.printStackTrace();
            try {
                // 可以选择拒绝消息（第三个参数requeue=false表示不重新入队,设置为true则消息会重新回到queue,broker会重新发送消息给消费端）
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
