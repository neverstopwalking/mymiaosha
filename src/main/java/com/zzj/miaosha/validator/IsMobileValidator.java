package com.zzj.miaosha.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.alibaba.druid.util.StringUtils;
import com.zzj.miaosha.util.ValidatorUtil;

public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {
	
	private boolean required;
	@Override
	public void initialize(IsMobile constraintAnnotation) {
		// TODO Auto-generated method stub
		required=constraintAnnotation.required();
	}
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		// TODO Auto-generated method stub
		if(required)
		{
			return ValidatorUtil.isMobile(value);
		}
		else
		{
			if(StringUtils.isEmpty(value))
			{
				return true;
			}
			else
			{
				return ValidatorUtil.isMobile(value);
			}
		}
	
	}

}
