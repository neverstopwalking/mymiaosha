package com.zzj.miaosha.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzj.miaosha.domain.MiaoShaUser;
import com.zzj.miaosha.domain.OrderInfo;
import com.zzj.miaosha.result.CodeMsg;
import com.zzj.miaosha.result.Result;
import com.zzj.miaosha.service.GoodsService;
import com.zzj.miaosha.service.OrderService;
import com.zzj.miaosha.vo.GoodsDetailVo;
import com.zzj.miaosha.vo.GoodsVo;
import com.zzj.miaosha.vo.OrderDetailVo;

@Controller
@RequestMapping("/order")
public class OrderController {
	
	@Autowired 
	OrderService orderService;
	@Autowired
	GoodsService goodsService;
	
	@RequestMapping("/detail")
	@ResponseBody
	public Result<OrderDetailVo> info(Model  model,MiaoShaUser user,@RequestParam("orderId") long orderId)
	{
		if(user==null)
			return Result.error(CodeMsg.Session_Error);
		
		OrderInfo order=orderService.getOrderById(orderId);
		if(order==null)
		{
			return Result.error(CodeMsg.ORDER_Not_Exist);
		}
		long goodsId=order.getGoodsId();
		GoodsVo goods=goodsService.getGoodsVoByGoodsId(goodsId);
		OrderDetailVo vo=new OrderDetailVo();
		vo.setGoods(goods);
		vo.setOrder(order);
		return Result.success(vo);
		
	}
}
