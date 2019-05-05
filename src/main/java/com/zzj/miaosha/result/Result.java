package com.zzj.miaosha.result;

public class Result<T> {
	private int code;
	private String message;
	private T data;
	private Result(T data) {
		// TODO Auto-generated constructor stub
		this.code=0;
		this.message="success";
		this.data=data;
		}
	public Result(CodeMsg cm) {
		// TODO Auto-generated constructor stub
		if(cm==null)
			return ;
		this.code=cm.getCode();
		this.message=cm.getMessage();
	}
	public int getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public T getData() {
		return data;
	}
	
	//成功的时候调用
	public static <T>Result<T> success(T data) {
		// TODO Auto-generated method stub
		return new Result<T>(data);
	}
	//失败的时候调用
	public static<T> Result<T> error(CodeMsg cm) {
		// TODO Auto-generated method stub
		
		return new Result<T>(cm);
	}

	
	 
}
