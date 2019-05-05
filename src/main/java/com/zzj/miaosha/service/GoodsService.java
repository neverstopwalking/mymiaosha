package com.zzj.miaosha.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzj.miaosha.dao.GoodsDao;
import com.zzj.miaosha.domain.Goods;
import com.zzj.miaosha.domain.MiaoshaGoods;
import com.zzj.miaosha.vo.GoodsVo;

@Service
public class GoodsService {
  @Autowired
   GoodsDao goodsDao;
  
  public List<GoodsVo> listGoodsVo()
  {
	  return goodsDao.listGoodsVo();
  }

public GoodsVo getGoodsVoByGoodsId(long goodsId) {
	// TODO Auto-generated method stub
	return  goodsDao.getGoodsVoByGoodsId(goodsId);
}

@Transactional
public boolean reduceStock(GoodsVo goods) {
	// TODO Auto-generated method stub
	
	MiaoshaGoods g=new MiaoshaGoods();
	g.setGoodsId(goods.getId());
	
	int ret=goodsDao.reduceStock(g);
	return ret>0;
	
}
  
  
  
}
