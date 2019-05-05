package com.zzj.miaosha.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.context.webflux.SpringWebFluxContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import com.alibaba.druid.util.StringUtils;
import com.zzj.miaosha.domain.MiaoShaUser;
import com.zzj.miaosha.domain.User;
import com.zzj.miaosha.redis.GoodsKey;
import com.zzj.miaosha.redis.MiaoShaUserKey;
import com.zzj.miaosha.redis.RedisService;
import com.zzj.miaosha.redis.UserKey;
import com.zzj.miaosha.result.CodeMsg;
import com.zzj.miaosha.result.Result;
import com.zzj.miaosha.service.GoodsService;
import com.zzj.miaosha.service.MiaoShaUserService;
import com.zzj.miaosha.service.UserService;
import com.zzj.miaosha.util.ValidatorUtil;
import com.zzj.miaosha.vo.GoodsDetailVo;
import com.zzj.miaosha.vo.GoodsVo;
import com.zzj.miaosha.vo.LoginVo;

@Controller
@RequestMapping("/goods")
public class GoodsController {
	private static Logger log=LoggerFactory.getLogger(GoodsController.class);
	@Autowired
	MiaoShaUserService userService;
	
	@Autowired
	RedisService redisService;
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	ThymeleafViewResolver thymeleafViewResolver;
	

	@RequestMapping(value="/to_list",produces="text/html")
	@ResponseBody
	
	public String list(Model model,HttpServletResponse response,HttpServletRequest request,MiaoShaUser user) {
		
		model.addAttribute("user", user);
		//取缓存
				String  html=redisService.get(GoodsKey.getGoodsList,"", String.class);
				if(!StringUtils.isEmpty(html))
				{
					return html;
				}
		//查询商品列表         
		List<GoodsVo> goodsList=goodsService.listGoodsVo();
		model.addAttribute("goodsList", goodsList);
		//return "goods_list";
		
		
		IWebContext context=new WebContext(request, response, request.getServletContext(),request.getLocale(),model.asMap()); 
		//手动渲染
		html=thymeleafViewResolver.getTemplateEngine().process("goods_List", context);
		if(!StringUtils.isEmpty(html))
		{
			redisService.set(GoodsKey.getGoodsList, "", html);
		}
		
		return html;
		
	}
	@RequestMapping(value="/to_detail2/{goodsId}", produces="text/html")
	@ResponseBody
		
		public String detail2(Model model,MiaoShaUser user,HttpServletResponse response,HttpServletRequest request,@PathVariable("goodsId") long goodsId) {
			
			model.addAttribute("user", user);
			//取缓存
			String  html=redisService.get(GoodsKey.getGoodsDetail,"", String.class);
			if(!StringUtils.isEmpty(html))
			{
				return html;
			}
			//查询商品详情
			GoodsVo goods=goodsService.getGoodsVoByGoodsId(goodsId);
			
			model.addAttribute("goods", goods);
			
			long startAt=goods.getStartDate().getTime();
			long endAt=goods.getEndDate().getTime();
			long now=System.currentTimeMillis();
			
			
			int miaoshaStatus=0;//秒杀状态
			int remainSeconds=0;//秒杀还有多少秒开始
			if(now<startAt)//秒杀未开始，倒计时
			{
				miaoshaStatus=0;
				remainSeconds=(int)(startAt-now)/1000;
				
			}else if(now >endAt) //秒杀结束
			{
				miaoshaStatus=2;
				remainSeconds=-1;
			}else {//秒杀进行中
				miaoshaStatus=1;
				remainSeconds=0;
			}
			
			model.addAttribute("miaoshaStatus", miaoshaStatus);
			model.addAttribute("remainSeconds", remainSeconds);
			//return "goods_detail";
			
			
					IWebContext context=new WebContext(request, response, request.getServletContext(),request.getLocale(),model.asMap()); 
					//手动渲染
					html=thymeleafViewResolver.getTemplateEngine().process("goods_Detail", context);
					if(!StringUtils.isEmpty(html))
					{
						redisService.set(GoodsKey.getGoodsDetail, "", html);
					}
					
					return html;
		} 
	
@RequestMapping(value="/detail/{goodsId}")
@ResponseBody
	
	public Result<GoodsDetailVo> detail(Model model,MiaoShaUser user,HttpServletResponse response,HttpServletRequest request,@PathVariable("goodsId") long goodsId) {
		
		
		
		//查询商品详情
		GoodsVo goods=goodsService.getGoodsVoByGoodsId(goodsId);
		
		
		
		long startAt=goods.getStartDate().getTime();
		long endAt=goods.getEndDate().getTime();
		long now=System.currentTimeMillis();
		
		
		int miaoshaStatus=0;//秒杀状态
		int remainSeconds=0;//秒杀还有多少秒开始
		if(now<startAt)//秒杀未开始，倒计时
		{
			miaoshaStatus=0;
			remainSeconds=(int)(startAt-now)/1000;
			
		}else if(now >endAt) //秒杀结束
		{
			miaoshaStatus=2;
			remainSeconds=-1;
		}else {//秒杀进行中
			miaoshaStatus=1;
			remainSeconds=0;
		}
		GoodsDetailVo vo=new GoodsDetailVo();
		vo.setGoods(goods);
		vo.setMiaoshaStatus(miaoshaStatus);
		vo.setRemainSeconds(remainSeconds);
		vo.setUser(user);
		
				
				return Result.success(vo);  
	}
	
	}
	
	

	

	
	
	


