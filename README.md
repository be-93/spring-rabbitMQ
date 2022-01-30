# # :loudspeaker: RabbitMQ

## :one: RabbitMQ 란

> 보통 클라이언트로부터 요청된 다수의 작업을 처리하려할 때 웹 애플리케이션 서버에서 모든 요청을 처리하기 보다는 MQ를 사용하여 처리를 다른 처리자에게 위임한다.     
> RabbitMQ는 이러한 작업을 처리할 수 있는 AMQP(Advenced Message Queing Protocol, MQ 표준 프로토콜)를    
> 따르는 여러 오픈소스 메시지 브로커 제품중 하나다.

## :two: 주요 요소

- Message

> 처리해야할 내용이 담겨져있다.

- Producer

> Producer는 message를 consumer에게 위임하기 위해 message를 exchange에 publish 하는 자이다.   
> message를 보내는 일 이외에는 아무일도 하지 않는다.

- Consumer

> Consumer는 message를 producer로 부터 위임 받아 처리하는 자이다.

- Exchange

> Exchange는 producer에서 전달받은 message를 queue에게 전달해준다.   
> exchange는 메시지를 어떤 queue에 추가할지? 또, 얼마나 추가할지? 아니면 그냥 버려야할지 이는 exchange 규칙에 의해 결정된다