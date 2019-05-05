package com.zzj.miaosha.redis;

public class GoodsKey extends BasePrefix {

	public GoodsKey( int expireSeconds,String prefix) {
		super(expireSeconds,prefix);
		// TODO Auto-generated constructor stub
	}
	
	public static GoodsKey getGoodsList=new GoodsKey(60,"gl");
	public static GoodsKey getGoodsDetail=new GoodsKey(60,"gd");
	public static GoodsKey getGoodsStock=new GoodsKey(0,"gs");
	

}
