package com.ar.common.service.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Transaction {
	public enum scope {
		METHOD,
		SERVICE,
		NONE
	} // enum scope
	
	scope value();
} // annotation Transaction
