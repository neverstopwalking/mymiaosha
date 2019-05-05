package com.zzj.miaosha.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
	
	public static String salt="1a2b3c4d";
	public static String md5(String src)
	{
		return DigestUtils.md5Hex(src);
	}
	
	
	//输入密码用md5加密
	public static String inputPassToFormPass(String inputPass)
	{
		String str=""+salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
		System.out.println(str);
		return md5(str);
	}
	
	
	//服务器对接收到的md5再一次用md5加密，存到数据库中
	public static String FormPassToDbPass(String FormPass,String salt)
	{
		String str=salt.charAt(0)+salt.charAt(2)+FormPass+salt.charAt(5)+salt.charAt(4)+"";
		return md5(str);
	}
	
	
	public static String  inputPassToDbPass(String inputPass,String salt)
	{
		String formPass=inputPassToFormPass(inputPass);
		String dbPass=FormPassToDbPass(formPass, salt);
		return dbPass;
	}
	
	
	public static void main(String []args)
	{
		System.out.println(inputPassToFormPass("123456"));  //ce21b747de5af71ab5c2e20ff0a60eea d3b1294a61a07da9b49b6e22b2cbd7f9
		System.out.println(FormPassToDbPass(inputPassToFormPass("123456"), salt));
		System.out.println(inputPassToDbPass("123456", salt));
	}

}
