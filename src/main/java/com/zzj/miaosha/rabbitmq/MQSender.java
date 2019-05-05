package com.zzj.miaosha.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzj.miaosha.redis.RedisService;

@Service
public class MQSender {
	
	private static Logger log=LoggerFactory.getLogger(MQSender.class);
	@Autowired 
	AmqpTemplate amqpTemplatel;
   public void send(Object message)
   {
	   String msg=RedisService.beanToString(message);
	   log.info("send message"+msg );
	   amqpTemplatel.convertAndSend(MQConfig.QUEUE, msg);
   }
  public void sendMiaoshaMessage(MiaoshaMessage message) {
	// TODO Auto-generated method stub
	  
	  String msg=RedisService.beanToString(message);
	   log.info("send message"+msg );
	   amqpTemplatel.convertAndSend(MQConfig.MIAOSHAQUEUE, msg);
	
  }
  
	
	

}
