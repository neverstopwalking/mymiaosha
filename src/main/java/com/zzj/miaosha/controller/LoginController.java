package com.zzj.miaosha.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.alibaba.druid.util.StringUtils;
import com.zzj.miaosha.domain.User;
import com.zzj.miaosha.redis.RedisService;
import com.zzj.miaosha.redis.UserKey;
import com.zzj.miaosha.result.CodeMsg;
import com.zzj.miaosha.result.Result;
import com.zzj.miaosha.service.MiaoShaUserService;
import com.zzj.miaosha.service.UserService;
import com.zzj.miaosha.util.ValidatorUtil;
import com.zzj.miaosha.vo.LoginVo;

@Controller
@RequestMapping("/login")
public class LoginController {
	private static Logger log=LoggerFactory.getLogger(LoginController.class);
	@Autowired
	MiaoShaUserService userService;
	
	@Autowired
	RedisService redisService;

	@RequestMapping("/to_login")
	
	public String toLogin() {
		return "login";
	}

	@RequestMapping("/do_login")
	@ResponseBody
	public Result<Boolean>  doLogin(HttpServletResponse response,@Valid LoginVo loginVo) {
		
		log.info(loginVo.toString());
		
//		//参数校验
//		String password=loginVo.getPassword();
//		String mobile=loginVo.getMobile();
//		if(StringUtils.isEmpty(password))
//		{
//			return Result.error(CodeMsg.Password_Empty);
//		}
//		if(StringUtils.isEmpty(mobile))
//		{
//			return Result.error(CodeMsg.Mobile_Empty);
//		}
//		if(!ValidatorUtil.isMobile(mobile))
//		{
//			return Result.error(CodeMsg.Mobile_Error);
//		}
		
		//登录
	   userService.login(response,loginVo);
		return Result.success(true);
	}
	
	

	

	
	
	

}
