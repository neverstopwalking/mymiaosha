package com.zzj.miaosha.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.alibaba.druid.util.StringUtils;
import com.zzj.miaosha.access.AccessLimit;
import com.zzj.miaosha.domain.MiaoShaUser;
import com.zzj.miaosha.domain.MiaoshaOrder;
import com.zzj.miaosha.domain.OrderInfo;
import com.zzj.miaosha.domain.User;
import com.zzj.miaosha.rabbitmq.MQSender;
import com.zzj.miaosha.rabbitmq.MiaoshaMessage;
import com.zzj.miaosha.redis.AcessKey;
import com.zzj.miaosha.redis.GoodsKey;
import com.zzj.miaosha.redis.MiaoShaKey;
import com.zzj.miaosha.redis.MiaoShaUserKey;
import com.zzj.miaosha.redis.RedisService;
import com.zzj.miaosha.redis.UserKey;
import com.zzj.miaosha.result.CodeMsg;
import com.zzj.miaosha.result.Result;
import com.zzj.miaosha.service.GoodsService;
import com.zzj.miaosha.service.MiaoShaUserService;
import com.zzj.miaosha.service.MiaoshaService;
import com.zzj.miaosha.service.OrderService;
import com.zzj.miaosha.service.UserService;
import com.zzj.miaosha.util.MD5Util;
import com.zzj.miaosha.util.UUIDUtil;
import com.zzj.miaosha.util.ValidatorUtil;
import com.zzj.miaosha.vo.GoodsVo;
import com.zzj.miaosha.vo.LoginVo;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {
	private static Logger log=LoggerFactory.getLogger(MiaoshaController.class);
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
	
	@Autowired 
	MQSender sender;
	
	
	private Map<Long,Boolean> localOverMap=new HashMap();
	/**
	 * 系统初始化
	 */

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		List<GoodsVo> goodsList=goodsService.listGoodsVo();
		if(goodsList==null)
			return ;
		for(GoodsVo goods:goodsList)
		{
			redisService.set(GoodsKey.getGoodsStock, ""+goods.getId(), goods.getStockCount());
			localOverMap.put(goods.getId(), false);
		}
		
		
		
		
		
	}
    /**
     * 
     * 返回orderId:成功
     * -1：秒杀失败
     * 0:还在排队中
     */
	@RequestMapping(value="/result",method=RequestMethod.GET)
	@ResponseBody
	public Result<Long> miaoshaResult(Model model,MiaoShaUser user,@RequestParam("goodsId")long goodsId) {
		
		model.addAttribute("user", user);
		if(user==null)
		{
			return Result.error(CodeMsg.Session_Error);
		}
		Long result=miaoshaService.getMiaoshaResult(user.getId(),goodsId);
		return Result.success(result);
	}
	@RequestMapping(value="/{path}/do_miaosha",method=RequestMethod.POST)
	@ResponseBody
	public Result<Integer> miaosha(Model model,MiaoShaUser user,@PathVariable("path") String path,@RequestParam("goodsId")long goodsId) {
		
		model.addAttribute("user", user);
		if(user==null)
		{
			return Result.error(CodeMsg.Session_Error);
		}
		
		//验证path
		boolean check=miaoshaService.checkPath(user,goodsId,path);
		if(!check)
		{
			return Result.error(CodeMsg.REQUEST_ILLEGAL);
		}
		
		
		//内存标记，减少redis访问
		boolean isOver=localOverMap.get(goodsId);
		if(isOver)
		{
			return Result.error(CodeMsg.MIAOSHA_FAILURE);
		}
		
		//redis预减缓存
		long stock=redisService.decr(GoodsKey.getGoodsStock, String.valueOf(goodsId));
		//判断库存是否充足
		if(stock<0)
		{
			localOverMap.put(goodsId, true);
			return Result.error(CodeMsg.MIAOSHA_FAILURE);
		}
		
		//判断是否已经秒杀到了
		MiaoshaOrder order=orderService.getGoodsByUserIdGoodsId(user.getId(),goodsId);
		if(order!=null)
		{
			return Result.error(CodeMsg.REPEATE_MIAOSHA);
		}
		//之前未秒杀到，库存充足，就入队
		MiaoshaMessage message=new MiaoshaMessage();
		message.setGoodsId(goodsId);
		message.setUser(user);
		sender.sendMiaoshaMessage(message);
		
		return Result.success(0);//排队中
		
	
		
		
	/*	//查询商品
		GoodsVo goods=goodsService.getGoodsVoByGoodsId(goodsId);
		//判断库存
		int stock=goods.getGoodsStock();
		if(stock<=0)
		{
			return Result.error(CodeMsg.MIAOSHA_FAILURE);
		}
		//判断是否已经秒杀到了
		MiaoshaOrder order=orderService.getGoodsByUserIdGoodsId(user.getId(),goodsId);
		if(order!=null)
		{
			return Result.error(CodeMsg.REPEATE_MIAOSHA);
		}
		
		
	//减库存，下订单，写入秒杀订单中
		OrderInfo orderInfo=miaoshaService.miaosha(user,goods);
		
		return Result.success(orderInfo);*/
	}
	
	@AccessLimit(seconds=5,maxCount=5,needLogin=true)
	@RequestMapping(value="/path",method=RequestMethod.GET)
	@ResponseBody
	public Result<String> getMiaoshaPath(HttpServletRequest request,MiaoShaUser user,
			@RequestParam("goodsId")long goodsId,@RequestParam(value="verifyCode",defaultValue="0")int verifyCode)
	{
		
		
		if(user==null)
		{
			return Result.error(CodeMsg.Session_Error);
		}
		
		//查询访问次数
		String uri=request.getRequestURI();
		String key=uri+"_"+user.getId();
		
		
		boolean check=miaoshaService.checkVerifyCode(user,goodsId,verifyCode);
		if(!check)
		{
			return Result.error(CodeMsg.REQUEST_ILLEGAL);
		}
		String path=miaoshaService.createMiaoshaPath(user,goodsId);
		
		return Result.success(path);
	}
	
	@RequestMapping(value="/verifyCode",method=RequestMethod.GET)
	@ResponseBody
	public Result<String> getMiaoshaVerifyCode(HttpServletResponse response,MiaoShaUser user,@RequestParam("goodsId")long goodsId) {
		
		
		if(user==null)
		{
			return Result.error(CodeMsg.Session_Error);
		}
		
		BufferedImage image=miaoshaService.createVerifyCode(user,goodsId);
		
		try {
			OutputStream out=response.getOutputStream();
			ImageIO.write(image, "JPEG", out);
			out.flush();
			out.close();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	
	}
	
	

	

	
	
	


