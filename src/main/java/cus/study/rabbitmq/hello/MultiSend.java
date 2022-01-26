package cus.study.rabbitmq.hello;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

@Slf4j
public class MultiSend {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();

        // 타 시스템에 연결하려면 localhost 부분에 IP 나 도메인 주소를 입력해주면 된다.
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection()){
            // API 가 존재하는 채널을 연결해줘야한다.
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            for (int i = 0; i < 10; i++) {
                String message = "Hello World Time [ " + LocalDateTime.now() + " ]";
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

                log.info("[X] Sent ==> {}", message);
                sleep(1000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
