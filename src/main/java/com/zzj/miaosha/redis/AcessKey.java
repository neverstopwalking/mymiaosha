package com.zzj.miaosha.redis;

public class AcessKey extends BasePrefix {
   
	
	public AcessKey(int expireSeconds,String prefix) {
		super(expireSeconds,prefix);
		// TODO Auto-generated constructor stub
	}
	
	
	
	public static AcessKey withExpire(int expireSeconds)
	{
		return new AcessKey(expireSeconds,"acess");
	}
	


}
