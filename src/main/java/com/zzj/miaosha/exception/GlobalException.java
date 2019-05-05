package com.zzj.miaosha.exception;

import com.zzj.miaosha.result.CodeMsg;

public class GlobalException extends RuntimeException {
	private CodeMsg cm;
	public GlobalException(CodeMsg cm) {
       this.cm=cm;
	}
	public CodeMsg getCm() {
		return cm;
	}
	
	
	
	
}
