package com.zzj.miaosha.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.zzj.miaosha.domain.MiaoShaUser;
import com.zzj.miaosha.domain.User;

@Mapper
public interface MiaoShaUserDao {
	@Select("select * from miaosha_user where id=#{id}")
	public MiaoShaUser getUserById(@Param("id")Long id);

	
	@Insert("insert into user(id,name) values(#{id},#{name})")
	public void insert(User u1);

	@Update("update miaosha_user set password=#{password} where id=#{id}")
	public void update(MiaoShaUser toBeUpdate);
	
	
	
}
