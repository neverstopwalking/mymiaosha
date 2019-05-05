package com.zzj.miaosha.exception;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzj.miaosha.result.CodeMsg;
import com.zzj.miaosha.result.Result;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
	
	
	@ExceptionHandler(value=Exception.class)
	public Result< String> exceptionHandler(HttpServletRequest request,Exception e)
	
	{
		if(e instanceof GlobalException)
		{
			GlobalException exception=(GlobalException)e;
			return Result.error(exception.getCm());
		}
		  
		 
		else  if(e instanceof BindException)
		 {
			 BindException ex=(BindException)e;
			 List<ObjectError> list=ex.getAllErrors();
			 ObjectError objectError=list.get(0);
			 String message=objectError.getDefaultMessage();
			 return Result.error(CodeMsg.BIND_ERROR.fillArgs(message));
		 }
		 else
			 return Result.error(CodeMsg.Server_Error);
			}

}
