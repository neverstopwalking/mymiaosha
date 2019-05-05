package com.zzj.miaosha.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.logging.log4j.util.Chars;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzj.miaosha.dao.GoodsDao;
import com.zzj.miaosha.domain.Goods;
import com.zzj.miaosha.domain.MiaoShaUser;
import com.zzj.miaosha.domain.MiaoshaOrder;
import com.zzj.miaosha.domain.OrderInfo;
import com.zzj.miaosha.redis.MiaoShaKey;
import com.zzj.miaosha.redis.RedisService;
import com.zzj.miaosha.util.MD5Util;
import com.zzj.miaosha.util.UUIDUtil;
import com.zzj.miaosha.vo.GoodsVo;
import com.zzj.miaosha.redis.MiaoShaKey;

@Service
public class MiaoshaService {
  @Autowired
   GoodsService goodsService;
  
  @Autowired
  OrderService orderService;
  @Autowired
  RedisService redisService;

  
 
@Transactional
public OrderInfo miaosha(MiaoShaUser user, GoodsVo goods) {
	// TODO Auto-generated method stub
	//减库存，下订单，写入秒杀订单
	
	boolean success=goodsService.reduceStock(goods);
	if(success)
	{
	return orderService.createOrder(user,goods);
	}
	else
	{
		
		setGoodsOver(goods.getId());
			return null;
	}
	
	
	
	
	
}



public long getMiaoshaResult(Long userId, long goodsId) {
	// TODO Auto-generated method stub
	MiaoshaOrder order=orderService.getGoodsByUserIdGoodsId(userId, goodsId);
	if(order!=null)
	{
		return order.getOrderId();
	}
	else {
		boolean isOver=getGoodsOver(goodsId);
		if(isOver)
		{
			return -1;
		}
		else
			return 0;
	}
}
private void setGoodsOver(long id) {
	// TODO Auto-generated method stub
	redisService.set(MiaoShaKey.IS_GOODOVER, ""+id, true);
}
private boolean getGoodsOver(long goodsId) {
	// TODO Auto-generated method stub
	return redisService.exisits(MiaoShaKey.IS_GOODOVER, ""+goodsId);
}



public boolean checkPath(MiaoShaUser user, long goodsId, String path) {
	// TODO Auto-generated method stub
	if(user==null||path==null)
		return false;
	
	String pathOld=redisService.get(MiaoShaKey.getMiaoshaPath, user.getId()+"_"+goodsId, String.class);
	
	return pathOld.equals(path);
	

}



public String createMiaoshaPath(MiaoShaUser user, long goodsId) {
	// TODO Auto-generated method stub
	String str=MD5Util.md5(UUIDUtil.uuid()+"123456");
	redisService.set(MiaoShaKey.getMiaoshaPath, ""+user.getId()+"_"+goodsId,str);
	return str;
}



public BufferedImage createVerifyCode(MiaoShaUser user, long goodsId) {
	// TODO Auto-generated method stub
	
	if(user==null)
	{
		return null;
	}
	int width = 80;
	int height = 32;
	//create the image
	BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	Graphics g = image.getGraphics();
	// set the background color
	g.setColor(new Color(0xDCDCDC));
	g.fillRect(0, 0, width, height);
	// draw the border
	g.setColor(Color.black);
	g.drawRect(0, 0, width - 1, height - 1);
	// create a random instance to generate the codes
	Random rdm = new Random();
	// make some confusion
	for (int i = 0; i < 50; i++) {
		int x = rdm.nextInt(width);
		int y = rdm.nextInt(height);
		g.drawOval(x, y, 0, 0);
	}
	// generate a random code
	String verifyCode = generateVerifyCode(rdm);
	g.setColor(new Color(0, 100, 0));
	g.setFont(new Font("Candara", Font.BOLD, 24));
	g.drawString(verifyCode, 8, 24);
	g.dispose();
	//把验证码存到redis中
	int rnd = calc(verifyCode);
	redisService.set(MiaoShaKey.getMiaoshaVerifyCode, user.getId()+","+goodsId, rnd);
	//输出图片
	return image;
}



  private  int calc(String verifyCode) {
	// TODO Auto-generated method stub
	
	try
	{
	   ScriptEngineManager manager=new ScriptEngineManager();
	  
	   ScriptEngine engine=manager.getEngineByName("JavaScript");
	   return (Integer)engine.eval(verifyCode);
	}catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
		return 0;
	}
	
}

private static char[]ops= {'+','-','*'};

private String generateVerifyCode(Random rdm) {
	// TODO Auto-generated method stub
	int rd1=rdm.nextInt(10);
	int rd2=rdm.nextInt(10);
	int rd3=rdm.nextInt(10);
	
	char op1=ops[rdm.nextInt(3)];
	char op2=ops[rdm.nextInt(3)];
	String exp=""+rd1+op1+rd2+op2+rd3;
	
	return exp;
}



public boolean checkVerifyCode(MiaoShaUser user, long goodsId, int verifyCode) {
	// TODO Auto-generated method stub
	Integer codeOld=redisService.get(MiaoShaKey.getMiaoshaVerifyCode, user.getId()+","+goodsId, Integer.class);
	if(codeOld==null||codeOld-verifyCode!=0)
	{
		return false;
	}
	redisService.delete(MiaoShaKey.getMiaoshaVerifyCode, user.getId()+","+goodsId);
	return true;
}
  
  
  
}
