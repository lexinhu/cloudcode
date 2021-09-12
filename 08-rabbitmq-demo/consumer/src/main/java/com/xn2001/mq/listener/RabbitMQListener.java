package com.xn2001.mq.listener;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Map;

/**
 * @author 乐心湖
 * @version 1.0
 * @date 2021/9/4 20:30
 */

@Component
public class RabbitMQListener {

    //@RabbitListener(queues = "simple.queue")
    //public void listenSimpleQueueMessage(String msg) throws InterruptedException {
    //    System.out.println("消费者接收到消息：【" + msg + "】");
    //}

    @RabbitListener(queuesToDeclare = @Queue(value = "simple.queue"))
    public void listenWorkQueue1(String msg) throws InterruptedException {
        System.out.println("消费者1接收到消息：【" + msg + "】" + LocalTime.now());
        Thread.sleep(20);
    }

    @RabbitListener(queuesToDeclare = @Queue(value = "simple.queue"))
    public void listenWorkQueue2(String msg) throws InterruptedException {
        System.err.println("消费者2接收到消息：【" + msg + "】" + LocalTime.now());
        Thread.sleep(200);
    }

    @RabbitListener(queuesToDeclare = @Queue(value = "object.queue"))
    public void listenObjectQueue(Map<String,Object> msg) throws InterruptedException {
        System.err.println("object接收到消息：【" + msg + "】" + LocalTime.now());
        Thread.sleep(200);
    }

    @RabbitListener(queues = "fanout.queue1")
    public void listenFanoutQueue1(String msg) throws InterruptedException {
        System.out.println("接收到fanout.queue1的消息：【" + msg + "】" + LocalTime.now());
    }

    @RabbitListener(queues = "fanout.queue2")
    public void listenFanoutQueue2(String msg) throws InterruptedException {
        System.err.println("接收到fanout.queue2的消息：【" + msg + "】" + LocalTime.now());
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "fanout.queue3"),
            exchange = @Exchange(value = "xn2001.fanout",type = "fanout")
    ))
    public void listenFanoutQueue3(String msg) {
        System.out.println("接收到fanout.queue3的消息：【" + msg + "】" + LocalTime.now());
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "direct.queue1"),
            exchange = @Exchange(value = "xn2001.direct"),
            key = {"a","b"}
    ))
    public void listenDirectQueue1(String msg){
        System.out.println("接收到direct.queue1的消息：【" + msg + "】" + LocalTime.now());
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "direct.queue2"),
            exchange = @Exchange(value = "xn2001.direct"),
            key = {"a","c"}
    ))
    public void listenDirectQueue2(String msg){
        System.out.println("接收到direct.queue2的消息：【" + msg + "】" + LocalTime.now());
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "topic.queue1"),
            exchange = @Exchange(value = "xn2001.topic",type = ExchangeTypes.TOPIC),
            key = {"china.#"}
    ))
    public void listenTopicQueue1(String msg){
        System.out.println("接收到topic.queue1的消息：【" + msg + "】" + LocalTime.now());
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "topic.queue2"),
            exchange = @Exchange(value = "xn2001.topic",type = ExchangeTypes.TOPIC),
            key = {"china.*"}
    ))
    public void listenTopicQueue2(String msg){
        System.out.println("接收到topic.queue2的消息：【" + msg + "】" + LocalTime.now());
    }

}
