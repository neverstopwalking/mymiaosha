package com.zzj.miaosha.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.zzj.miaosha.dao.MiaoShaUserDao;
import com.zzj.miaosha.dao.UserDao;
import com.zzj.miaosha.domain.MiaoShaUser;
import com.zzj.miaosha.domain.User;
import com.zzj.miaosha.exception.GlobalException;
import com.zzj.miaosha.redis.MiaoShaUserKey;
import com.zzj.miaosha.redis.RedisService;
import com.zzj.miaosha.result.CodeMsg;
import com.zzj.miaosha.util.MD5Util;
import com.zzj.miaosha.util.UUIDUtil;
import com.zzj.miaosha.vo.LoginVo;

@Service

public class MiaoShaUserService {
	@Autowired
	MiaoShaUserDao miaoShaUserDao;
	
	@Autowired 
	RedisService redisService;
	public MiaoShaUser getUserById(long id)
	{
		
		//取缓存
		MiaoShaUser user=redisService.get(MiaoShaUserKey.getById, ""+id, MiaoShaUser.class);
		//System.out.println(user);
		if(user!=null)
		{
			return user;
		}
		//取数据库
		user= miaoShaUserDao.getUserById(id);
		//System.out.println("11"+user);
		if(user!=null)
		{
			//放进redis缓存中
			redisService.set(MiaoShaUserKey.getById, ""+id, user);
			
			
		}
		return user;
	}
	
	
	public boolean updatePassword(String token,long id,String formPass)
	{
		//取user
		MiaoShaUser user=getUserById(id);
		if(user==null)
		{
			throw new GlobalException(CodeMsg.Mobile_Not_Exist);
		}
		
		//更新数据库
		MiaoShaUser toBeUpdate=new MiaoShaUser();
		toBeUpdate.setId(id);
		toBeUpdate.setPassword(MD5Util.FormPassToDbPass(formPass, user.getSalt()));
		miaoShaUserDao.update(toBeUpdate);
		//删除缓存
		redisService.delete(MiaoShaUserKey.getById, ""+id);
		user.setPassword(toBeUpdate.getPassword());
		redisService.set(MiaoShaUserKey.token, token, user);
		
		return true;
		
		
	}
	public Boolean login(HttpServletResponse response,LoginVo loginVo) {
		// TODO Auto-generated method stub
		if(loginVo==null)
		     throw new GlobalException(CodeMsg.Server_Error);
		//System.out.println(loginVo);
		String mobile=loginVo.getMobile();
		 String formPass=loginVo.getPassword();
		 
		 //判断手机号是否存在
		 MiaoShaUser user=getUserById(Long.parseLong(mobile));
		 if(user==null)
			 throw new GlobalException (CodeMsg.Mobile_Not_Exist);
		 //验证密码
		 String dbPass=user.getPassword();
		 String salt=user.getSalt();
		String calPass= MD5Util.FormPassToDbPass(formPass, salt);
		if(!calPass.equals(dbPass))
		{
			throw new GlobalException(CodeMsg.Password_Error);
		}
		
		//生成cookie
		String token=UUIDUtil.uuid();
		addCookie(response, token,user);
	      return true;
	}
	
	private void addCookie(HttpServletResponse response,String token,MiaoShaUser user)
	{
	
		
		redisService.set(MiaoShaUserKey.token, token, user);
		Cookie cookie=new Cookie(MiaoShaUserKey.COOKIE_NAME_TOOKEN, token);
		cookie.setMaxAge(MiaoShaUserKey.token.expireSeconds());
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	public MiaoShaUser getByToken(HttpServletResponse response,String token) {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(token))
		{
			return null;
		}
		
		//延长有效期
		//生成cookie
	
		MiaoShaUser user= redisService.get(MiaoShaUserKey.token, token, MiaoShaUser.class); 
		if(user!=null)
		addCookie(response, token,user);
		
		return user;
	}
	
	
	
}
