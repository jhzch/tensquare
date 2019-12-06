package com.jh.test;

import com.jh.rabbitmq.RabbitApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RabbitApplication.class)
public class RabbitmqTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void sendTest(){
        //routingKey:队列的唯一标识
        rabbitTemplate.convertAndSend("itcast","直接模式测试");
    }

    @Test
    public void sendTest2(){
        //routingKey:队列的唯一标识
        rabbitTemplate.convertAndSend("chuanzhi","","分裂模式");
    }

    @Test
    public void sendTest3(){
        //routingKey:队列的唯一标识
        rabbitTemplate.convertAndSend("topic84","good.abc","主题模式");
    }


}
