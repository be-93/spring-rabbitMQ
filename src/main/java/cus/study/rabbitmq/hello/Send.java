package cus.study.rabbitmq.hello;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

@Slf4j
public class Send {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();

        // 타 시스템에 연결하려면 localhost 부분에 IP 나 도메인 주소를 입력해주면 된다.
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection()){
            // API 가 존재하는 채널을 연결해줘야한다.
            Channel channel = connection.createChannel();

            /**
             * queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String,Object> arguments)
             * @Parameters 정보
             * queue - 큐의 이름
             * durable- 지속성 대기열을 선언하는 경우 true(대기열은 서버를 다시 시작해도 유지됨)
             * exclusive- 배타적 큐를 선언하는 경우 true(이 연결로 제한됨)
             * autoDelete- 자동 삭제 대기열을 선언하는 경우 true(더 이상 사용하지 않을 때 서버에서 삭제함)
             * arguments- 큐에 대한 기타 속성(구성 인수)
             * */
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            String message = "Hello World Time [ " + LocalDateTime.now() + " ]";

            /**
             * 대기열을 선언하는 것은 멱등적이다. 대기열이 이미 존재하지 않는 경우에만 생성된다
             * basicPublish(String exchange, String routingKey, AMQP.BasicProperties props, byte[] body)
             * @Parameters 정보
             * exchange - 메시지를 게시할 교환기
             * routingKey - 라우팅 키
             * props - 메시지에 대한 기타 속성 - 라우팅 헤더 등
             * body - 메시지 본문
             * */
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

            log.info("[X] Sent ==> {}", message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
