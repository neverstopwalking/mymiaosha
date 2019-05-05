package com.zzj.miaosha.access;


import java.io.OutputStream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.zzj.miaosha.domain.MiaoShaUser;
import com.zzj.miaosha.redis.AcessKey;
import com.zzj.miaosha.redis.MiaoShaUserKey;
import com.zzj.miaosha.redis.RedisService;
import com.zzj.miaosha.result.CodeMsg;
import com.zzj.miaosha.result.Result;
import com.zzj.miaosha.service.MiaoShaUserService;
import com.zzj.miaosha.service.UserService;

@Service
public class AccessInterceptor extends  HandlerInterceptorAdapter{
	
	@Autowired 
	MiaoShaUserService userService;
	
	@Autowired
	RedisService redisService;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if(handler instanceof HandlerMethod)
		{
			MiaoShaUser user=getUser(response, request);
			UserContext.setUser(user);
			
			
			HandlerMethod hm=(HandlerMethod)handler;
			AccessLimit accessLimit=hm.getMethodAnnotation(AccessLimit.class);
			if(accessLimit==null)
			{
				return true;
			}
			int seconds=accessLimit.seconds();
			int maxCount=accessLimit.maxCount();
			boolean needLogin=accessLimit.needLogin();
			String key=request.getRequestURI();
			
			if(needLogin)
			{
				if(user==null)
				{
					render(response,CodeMsg.Session_Error);
					return false;
				}
				
				key+="_"+user.getId();
			}else
			{
				
			}
			
			Integer count=redisService.get(AcessKey.withExpire(seconds), key, Integer.class);
			if(count==null)
			{
				redisService.set(AcessKey.withExpire(seconds), key, 1);
			}
			else if(count<maxCount)
			{
				redisService.incr(AcessKey.withExpire(seconds), key);
			}
			else {
				 render(response,CodeMsg.Acess_Limit);
				 return false;
			}
		}
		return true;
	}
	
	

	private void render(HttpServletResponse response, CodeMsg cm)throws Exception {
		// TODO Auto-generated method stub
		response.setContentType("application/json;charset=UTF-8");
	OutputStream out=	response.getOutputStream();
	String str=JSON.toJSONString(Result.error(cm));
	out.write(str.getBytes("UTF-8"));
	out.flush();
	out.close();
		
	}



	private MiaoShaUser getUser(HttpServletResponse response,HttpServletRequest request) {
		String paramToken=request.getParameter(MiaoShaUserKey.COOKIE_NAME_TOOKEN);
		String cookieToken=getCookieValue(request,MiaoShaUserKey.COOKIE_NAME_TOOKEN);
		if(StringUtils.isEmpty(paramToken)&&StringUtils.isEmpty(cookieToken))
		{
			return null;
		}
		String token=StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
		return userService.getByToken(response,token);
		
		
	}
	
	private String getCookieValue(HttpServletRequest request, String cookieName) {
		// TODO Auto-generated method stub
		Cookie []cookies=request.getCookies();
		if(cookies==null||cookies.length<=0)
			return null;
		for(Cookie cookie:cookies)
		{
			if(cookie.getName().equals(cookieName))
				return cookie.getValue();
		}
		
		return null;
	}

}
