package cus.study.rabbitmq.hello;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class Worker {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        /**
         * RabbitMQ 서버가 종료되거나 충돌로 인해 멈추었을경우 Message 는 모두 손실된다.
         * 이를 방지하고자 producer 와 consumer 의 durable 값을 true 로 변경해줘야한다.
         * */
        boolean durable = true;

        /**
         * 두 개의 작업자가 있는 상황에서 어떤 작업은 처리하는데 오래 걸리고 어떤 것은 오래걸리지 않을때
         * RabbitMQ 는 이를 공평하게 작업을 배분해 줄 수 없다. 이 같은 현상이 발생하는 이유는
         * RabbitMQ 는 Message 가 대기열에 들어갈 때 Message 를 n 번째 Message 를 n 번째 consumer 에게 맹목적으로 발송하기 때문이다.
         * 이를 방지하기 위해 channel.basicQos(prefetchCount); 메소드를 이용하여 작업자에게 한 번에 하나 이상의 Message 를
         * 발송하지 않도록 RabbitMQ 에게 설정한다.
         * 즉 이전 Message 가 처리되고 승인 할 때까지 새로운 Message 를 발송하지 않도록 하는것이다.
         * */
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);
        
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        log.info("[*] Waiting for messages. ==> To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            log.info(" [x] Received ==> {}", message);
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }

}
