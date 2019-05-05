package com.zzj.miaosha.config;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.alibaba.druid.util.StringUtils;
import com.zzj.miaosha.access.UserContext;
import com.zzj.miaosha.domain.MiaoShaUser;
import com.zzj.miaosha.redis.MiaoShaUserKey;
import com.zzj.miaosha.service.MiaoShaUserService;
import com.zzj.miaosha.service.UserService;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver{
  
	
	@Autowired 
	MiaoShaUserService userService;
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory arg3) throws Exception {
		// TODO Auto-generated method stub
		return UserContext.getUser();
		
		
		
	}

	

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		// TODO Auto-generated method stub
		
      Class<?> clazz=parameter.getParameterType();		
		return clazz==MiaoShaUser.class;
	}

}
