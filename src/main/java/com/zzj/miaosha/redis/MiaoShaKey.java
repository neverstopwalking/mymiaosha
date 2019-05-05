package com.zzj.miaosha.redis;

public class MiaoShaKey extends BasePrefix {
   
	
	public MiaoShaKey(int expireSeconds,String prefix) {
		super(expireSeconds,prefix);
		// TODO Auto-generated constructor stub
	}
	
	public static MiaoShaKey IS_GOODOVER=new MiaoShaKey(0,"go");
	public static MiaoShaKey getMiaoshaPath=new MiaoShaKey(60,"mp");
	public static MiaoShaKey getMiaoshaVerifyCode=new MiaoShaKey(300,"vc");


}
