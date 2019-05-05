package com.zzj.miaosha.result;

import org.apache.ibatis.javassist.compiler.ast.IntConst;

public class CodeMsg {
	
	
	
	//通用异常
	public  static CodeMsg  Success=new CodeMsg(0,"success");
	public static CodeMsg  Server_Error = new CodeMsg(500100,"服务器异常");
	public static CodeMsg  BIND_ERROR = new CodeMsg(500101,"参数校验异常:%s");
	public static CodeMsg  REQUEST_ILLEGAL = new CodeMsg(500102,"请求非法");
	public static CodeMsg  Acess_Limit = new CodeMsg(500103,"访问太频繁");
	
  //登录模块5002XX
	public static CodeMsg  Session_Error = new CodeMsg(500210,"session不存在或已失效");
	public static CodeMsg  Password_Empty = new CodeMsg(500211,"密码不能为空");
	public static CodeMsg  Mobile_Empty = new CodeMsg(500212,"手机号不能为空");
	public static CodeMsg  Mobile_Error = new CodeMsg(500213,"手机号格式错误");
	public static CodeMsg Mobile_Not_Exist = new CodeMsg(500214,"手机号不存在");
	public static  CodeMsg Password_Error = new CodeMsg(500215,"密码错误");
	
	//商品模块
	//订单模块 5004XX
	public static CodeMsg ORDER_Not_Exist = new CodeMsg(500400,"订单不存在");
	//秒杀模块
	public static  CodeMsg MIAOSHA_FAILURE = new CodeMsg(500500,"秒杀已经秒杀完毕");
	public static  CodeMsg REPEATE_MIAOSHA = new CodeMsg(500501,"不能重复秒杀");
	private int code;
	private String  message;
	public CodeMsg(int code, String message) {
		// TODO Auto-generated constructor stub
		
		 this.code=code;
		 this.message=message;
	}
	public int getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	
	public CodeMsg fillArgs(Object...args)
	{
		int code=this.code;
		String message=String.format(this.message, args);
		return new CodeMsg(code, message);
	}
	
	

}
