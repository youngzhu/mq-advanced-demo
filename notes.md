# 如何保证消息的可靠性

## 1. 生产者确认机制：确保消息能到达队列
RabbitMQ提供了publisher confirm机制来避免消息发送到MQ过程中丢失。这种机制必须给每个消息指定一个唯一ID。消息发送到MQ以后，会返回一个结果给发送者，表示消息是否发送成功。

返回结果有两种方式：

- publisher-confirm，发送者确认
    - 消息成功投递到交换机，返回ack
    - 消息未投递到交换机，返回nack
- publisher-return，发送者回执
    - 消息投递到交换机了，但是没有路由到队列。返回ACK，及路由失败原因。

### 1.1 修改配置

首先，修改publisher服务中的application.yml文件，添加下面的内容：

```yaml
spring:
  rabbitmq:
    publisher-confirm-type: correlated
    publisher-returns: true
    template:
      mandatory: true
   
```

说明：
- `publish-confirm-type`：开启publisher-confirm，这里支持两种类型：
    - `simple`：同步等待confirm结果，直到超时
    - `correlated`：异步回调，定义ConfirmCallback，MQ返回结果时会回调这个ConfirmCallback
- `publish-returns`：开启publish-return功能，同样是基于callback机制，不过是定义ReturnCallback
- `template.mandatory`：定义消息路由失败时的策略。true，则调用ReturnCallback；false：则直接丢弃消息

### 1.2 定义Return回调

每个RabbitTemplate只能配置一个ReturnCallback，因此需要在项目加载时配置：

修改publisher服务，添加一个配置类：com.youngzy.mq.config.CommonConfig

### 1.3 定义ConfirmCallback

ConfirmCallback可以在发送消息时指定，因为每个业务处理confirm成功或失败的逻辑不一定相同。

在publisher服务中，创建一个单元测试方法：[testSendMessage2SimpleQueue()](publisher/src/test/java/com/youngzy/mq/spring/SpringAmqpTest.java)

## 2. 消息持久化：防止MQ重启导致消息丢失
## 3. 消费者确认机制：消费成功则返回ack
## 4. 消费者失败重试机制：