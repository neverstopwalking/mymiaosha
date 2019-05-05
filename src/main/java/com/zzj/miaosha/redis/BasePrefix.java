package com.zzj.miaosha.redis;

public abstract class BasePrefix implements KeyPrefix {

	
	 private int expireSeconds;//过期时间
	 private String prefix;  //前缀
	 
	 
	 public BasePrefix(String prefix)
	 {
		 this(0, prefix);
	 }
	  public BasePrefix(int expireSeconds,String prefix) {
		// TODO Auto-generated constructor stub
		  this.expireSeconds=expireSeconds;
		  this.prefix=prefix;
		  
	}
	@Override
	public int expireSeconds() {//默认0代表永不过期
		// TODO Auto-generated method stub
		return expireSeconds;
	}

	@Override
	public String getPrefix() {
		// TODO Auto-generated method stub
		String className=getClass().getSimpleName();
		return className+":"+prefix;
	}
	
	

}
