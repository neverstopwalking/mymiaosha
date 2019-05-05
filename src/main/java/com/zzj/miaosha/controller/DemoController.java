package com.zzj.miaosha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.zzj.miaosha.domain.User;
import com.zzj.miaosha.rabbitmq.MQSender;
import com.zzj.miaosha.redis.RedisService;
import com.zzj.miaosha.redis.UserKey;
import com.zzj.miaosha.result.CodeMsg;
import com.zzj.miaosha.result.Result;
import com.zzj.miaosha.service.UserService;

@Controller
@RequestMapping("/demo")
public class DemoController {
	@Autowired
	UserService userService;
	@Autowired
	MQSender mqSender;
	@Autowired
	RedisService redisService;

	@RequestMapping("/")
	@ResponseBody
	String home() {
		return "Hello World!";
	}

	@RequestMapping("/hello")
	@ResponseBody
	Result<String>  hello() {
		return Result.success("hello,springboot");
	}
	
	@RequestMapping("/mq")
	@ResponseBody
	Result<String>  mq() {
		mqSender.send("hello,xiaozhou");
		return Result.success("hello,springboot");
	}
	
	@RequestMapping("/db/get")
	@ResponseBody
	public Result<User> dbget() {
		
		return Result.success(userService.getUserById(1));
	}

	
	@RequestMapping("/db/tx")
	@ResponseBody
	public Result<Boolean> dbTx() {
		userService.tx();
		return Result.success(true);
	}
	
	
	@RequestMapping("/redis/get")
	@ResponseBody
	public Result<String> redisGet() {
	    boolean v1=redisService.set(UserKey.getById,"key2", "hello,lili");
	    if(v1)
	    {
	    	String string=redisService.get(UserKey.getById,"key2", String.class);
	    	return Result.success(string);
	    }
	    return null;
		
	}

}
