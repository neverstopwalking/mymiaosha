package com.zzj.miaosha.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import com.zzj.miaosha.domain.MiaoshaOrder;
import com.zzj.miaosha.domain.OrderInfo;
import com.zzj.miaosha.vo.GoodsVo;

@Mapper
public interface OrderDao {
	
	
	@Select("select * from miaosha_order where user_Id=#{userId} and goods_Id=#{goodsId}")
	public MiaoshaOrder getGoodsByUserIdGoodsId(@Param("userId")Long userId, @Param("goodsId")long goodsId);

	
	@Insert("insert into order_info(user_id,goods_id,goods_name,goods_count,goods_price,order_channel,status,create_date)values("
			+ "#{userId},#{goodsId},#{goodsName},#{goodsCount},#{goodsPrice},#{orderChannel},#{status},#{createDate})")
	@SelectKey(keyColumn="id",keyProperty="id",resultType=long.class,before=false,statement="select last_insert_id()")
	public Long insert(OrderInfo orderInfo);

	
	@Insert("insert into miaosha_order(user_id,goods_id,order_id)values(#{userId},#{goodsId},#{orderId})")
	public void insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);

	@Select("select * from order_info where id=#{orderId}")
	public OrderInfo getOrderById(@Param("orderId")long orderId);
	

}
