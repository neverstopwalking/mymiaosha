package com.zzj.miaosha.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.zzj.miaosha.domain.MiaoShaUser;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {
	@Autowired
	JedisPool jedisPool;
	
	//获取单个对象
	public <T> T get(KeyPrefix keyPrefix,String key,Class<T> clazz)
	{
		Jedis jedis=null;
		try
		     {
				jedis=jedisPool.getResource();
				String realKey=keyPrefix.getPrefix()+key;
				String str=jedis.get(realKey);
				T t=stringToBean(str,clazz);
				
				return t;
		     }finally {
				returnToPool(jedis);
			}
		
	}
	
	//设置单个对象
	public <T> Boolean set(KeyPrefix keyPrefix,String key,T value)
	{
		Jedis jedis=null;
		try
		     {
				jedis=jedisPool.getResource();
				String str=beanToString(value);
				if(str==null||str.length()<=0)
					return false;
				String  realKey=keyPrefix.getPrefix()+key;
				
				int expireSeconds=keyPrefix.expireSeconds();
				if(expireSeconds<=0)
				{
					jedis.set(realKey, str);
				}
				else
				{
					jedis.setex(realKey, expireSeconds, str);
				}
				
				
				return true;
		     }finally {
				returnToPool(jedis);
			}
		
	}
	
   //判断key是否存在           
	public <T> Boolean exisits(KeyPrefix keyPrefix,String key)
	{
		Jedis jedis=null;
		try
		     {
				jedis=jedisPool.getResource();
				
				
				String  realKey=keyPrefix.getPrefix()+key;
				
				
				 return jedis.exists(realKey);
		     }finally {
				returnToPool(jedis);
			}
		
	}
	
	//删除key
	public boolean delete(KeyPrefix keyPrefix,String key)
	{
		Jedis jedis=null;
		try
		     {
				jedis=jedisPool.getResource();
				
				
				String  realKey=keyPrefix.getPrefix()+key;
				Long ret=jedis.del(realKey);
				return ret>0;
				
			
		     }finally {
				returnToPool(jedis);
			}
		
	}
	//增加key值
	public <T> Long incr(KeyPrefix keyPrefix,String key)
	{
		Jedis jedis=null;
		try
		     {
				jedis=jedisPool.getResource();
				
				
				String  realKey=keyPrefix.getPrefix()+key;
				
				return jedis.incr(realKey);
				
		     }finally {
				returnToPool(jedis);
			}
		
	}
	
	
	//减少key值
	public <T> Long decr(KeyPrefix keyPrefix,String key)
	{
		Jedis jedis=null;
		try
		     {
				jedis=jedisPool.getResource();
				
				
				String  realKey=keyPrefix.getPrefix()+key;
				
				
				return jedis.decr(realKey);
		     }finally {
				returnToPool(jedis);
			}
		
	}
	public static  <T>String beanToString(T value) {
		// TODO Auto-generated method stub
		if(value==null)
			return null;
		Class<?> clazz=value.getClass();
		if(clazz==int.class||clazz==Integer.class)
		{
			return value+"";
		}else if (clazz==long.class||clazz==Long.class) {
			
			return value+"";
		}else if(clazz==String.class)
		{
			return (String)value;
		}else {
			
			return JSON.toJSONString(value);
		}
 		
		
	}

	//@SuppressWarnings("unchecked")
	public static  <T>T stringToBean(String str,Class<T> clazz) {
		// TODO Auto-generated method stub
		if(str==null||str.length()<=0||clazz==null)
			return null;
		if(clazz==int.class||clazz==Integer.class)
		{
			return (T)Integer.valueOf(str);
		}else if (clazz==long.class||clazz==Long.class) {
			
			return (T)Long.valueOf(str);
		}else if(clazz==String.class)
		{
			return (T)str;
		}else {
			
			return JSON.toJavaObject(JSON.parseObject(str), clazz);
		}
		
		
	}
	private void returnToPool(Jedis jedis) {
		// TODO Auto-generated method stub
		if(jedis!=null)
			jedis.close();
		
	}

	
	
	
}
