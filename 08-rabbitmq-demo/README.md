# RabbitMQ消息队列

## 概念

channel：操作 MQ 的工具

exchange：路由消息到队列中

queue：缓存消息

virtual host：虚拟主机，是对 queue、exchange 等资源的逻辑分组

## SpringAMQP

Basic Queue 简单队列模型

Work Queue 工作队列模型

发布、订阅模型-Fanout

发布、订阅模型-Direct

发布、订阅模型-Topic

## 消息转换器

**SpringAMQP 中消息的序列化和反序列化是怎么实现的？**

利用 MessageConverter 实现的，默认是JDK的序列化

注意发送方与接收方必须使用相同的 MessageConverter

