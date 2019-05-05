package com.zzj.miaosha.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzj.miaosha.dao.GoodsDao;
import com.zzj.miaosha.dao.OrderDao;
import com.zzj.miaosha.domain.MiaoShaUser;
import com.zzj.miaosha.domain.MiaoshaOrder;
import com.zzj.miaosha.domain.OrderInfo;
import com.zzj.miaosha.vo.GoodsVo;

@Service
public class OrderService {
 
  
  @Autowired 
  OrderDao orderDao;
  
  

public MiaoshaOrder getGoodsByUserIdGoodsId(Long userId, long goodsId) {
	// TODO Auto-generated method stub
	return orderDao.getGoodsByUserIdGoodsId(userId,goodsId);
	
	
}


@Transactional
public OrderInfo createOrder(MiaoShaUser user, GoodsVo goods) {
	// TODO Auto-generated method stub\
	
	OrderInfo orderInfo=new OrderInfo();
	orderInfo.setCreateDate(new Date());
	orderInfo.setDeliveryAddrId(0L);
	orderInfo.setGoodsCount(1);
	orderInfo.setGoodsId(goods.getId());
	orderInfo.setGoodsName(goods.getGoodsName());
	orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
	orderInfo.setOrderChannel(1);
	orderInfo.setStatus(0);
	orderInfo.setUserId(user.getId());
	
	orderDao.insert(orderInfo);
	
	MiaoshaOrder miaoshaOrder=new MiaoshaOrder();
	miaoshaOrder.setOrderId(orderInfo.getId());
	miaoshaOrder.setGoodsId(goods.getId());
	miaoshaOrder.setUserId(user.getId());
	
	orderDao.insertMiaoshaOrder(miaoshaOrder);
	
	return orderInfo;
}


public OrderInfo getOrderById(long orderId) {
	// TODO Auto-generated method stub
	return orderDao.getOrderById(orderId);
}
  
  
  
}
