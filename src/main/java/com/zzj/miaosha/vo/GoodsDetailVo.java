package com.zzj.miaosha.vo;

import com.zzj.miaosha.domain.MiaoShaUser;

public class GoodsDetailVo {
	
	private GoodsVo goods;
	private int miaoshaStatus;
	private int remainSeconds;
	private MiaoShaUser user;
	
	public MiaoShaUser getUser() {
		return user;
	}
	public void setUser(MiaoShaUser user) {
		this.user = user;
	}
	public GoodsVo getGoods() {
		return goods;
	}
	public void setGoods(GoodsVo goods) {
		this.goods = goods;
	}
	public int getMiaoshaStatus() {
		return miaoshaStatus;
	}
	public void setMiaoshaStatus(int miaoshaStatus) {
		this.miaoshaStatus = miaoshaStatus;
	}
	public int getRemainSeconds() {
		return remainSeconds;
	}
	public void setRemainSeconds(int remainSeconds) {
		this.remainSeconds = remainSeconds;
	}
	

}
