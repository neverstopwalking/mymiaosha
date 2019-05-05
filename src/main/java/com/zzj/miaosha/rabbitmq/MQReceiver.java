package com.zzj.miaosha.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzj.miaosha.domain.MiaoShaUser;
import com.zzj.miaosha.domain.MiaoshaOrder;
import com.zzj.miaosha.redis.RedisService;
import com.zzj.miaosha.service.GoodsService;
import com.zzj.miaosha.service.MiaoShaUserService;
import com.zzj.miaosha.service.MiaoshaService;
import com.zzj.miaosha.service.OrderService;
import com.zzj.miaosha.vo.GoodsVo;

@Service
public class MQReceiver {
	
	
	@Autowired
	MiaoShaUserService userService;
	
	@Autowired
	RedisService redisService;
	@Autowired
	GoodsService goodsService;
	@Autowired
      OrderService orderService;
	@Autowired
	MiaoshaService miaoshaService;
	
	
	private static Logger log=LoggerFactory.getLogger(MQReceiver.class);
	@RabbitListener(queues=MQConfig.MIAOSHAQUEUE)
	public void receive(String message)
	{
		log.info("receive message"+message);
		MiaoshaMessage msg=RedisService.stringToBean(message, MiaoshaMessage.class);
		MiaoShaUser user=msg.getUser();
		long goodsId=msg.getGoodsId();
		
		GoodsVo goods=goodsService.getGoodsVoByGoodsId(goodsId);
		long stock=goods.getStockCount();
		if(stock<0)
		{
			return ;
		}
		//判断是否重复秒杀
		MiaoshaOrder order=orderService.getGoodsByUserIdGoodsId(user.getId(), goodsId);
		if(order!=null)
		{
			return ;
		}
		//减库存，下订单，写入秒杀订单
		miaoshaService.miaosha(user, goods);
		
		
	}

}
