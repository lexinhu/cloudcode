package com.xn2001.mq.helloworld;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAmqpTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Test
    public void testSimpleQueue() {
        // 队列名称
        String queueName = "simple.queue";
        // 消息
        String message = "你好啊，乐心湖！";
        // 发送消息
        rabbitTemplate.convertAndSend(queueName, message);
    }
    /**
     * workQueue
     * 向队列中不停发送消息，模拟消息堆积。
     */
    @Test
    public void testWorkQueue() throws InterruptedException {
        // 队列名称
        String queueName = "simple.queue";
        // 消息
        String message = "hello, message";
        for (int i = 0; i < 50; i++) {
            // 发送消息
            rabbitTemplate.convertAndSend(queueName, message + i);
            Thread.sleep(20);
        }
    }

    /**
     * fanout
     * 向交换机发送消息
     */
    @Test
    public void testFanoutExchange() {
        // 交换机名称
        String exchangeName = "xn2001.fanout";
        // 消息
        String message = "hello, everybody!";
        rabbitTemplate.convertAndSend(exchangeName, "", message);
    }

    /**
     * direct
     * 向交换机发送消息
     */
    @Test
    public void testDirectExchangeToA() {
        // 交换机名称
        String exchangeName = "xn2001.direct";
        // 消息
        String message = "hello, i am direct to a!";
        rabbitTemplate.convertAndSend(exchangeName, "a", message);
    }

    /**
     * direct
     * 向交换机发送消息
     */
    @Test
    public void testDirectExchangeToB() {
        // 交换机名称
        String exchangeName = "xn2001.direct";
        // 消息
        String message = "hello, i am direct to b!";
        rabbitTemplate.convertAndSend(exchangeName, "b", message);
    }

    /**
     * topic
     * 向交换机发送消息
     */
    @Test
    public void testTopicExchange() {
        // 交换机名称
        String exchangeName = "xn2001.topic";
        // 消息
        String message1 = "hello, i am topic form china.news";
        String message2 = "hello, i am topic form china.news.2";
        rabbitTemplate.convertAndSend(exchangeName, "china.news", message1);
        rabbitTemplate.convertAndSend(exchangeName, "china.news.2", message2);
    }

    @Test
    public void testSendMap()  {
        // 准备消息
        Map<String,Object> msg = new HashMap<>();
        msg.put("name", "Jack");
        msg.put("age", 21);
        // 发送消息
        rabbitTemplate.convertAndSend("object.queue", msg);
    }

}
