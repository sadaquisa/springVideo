package com.ingenieriaweb.springboot.app.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


@Constraint(validatedBy=ClienteDniValidation.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ClienteDni {
	
	String message() default "EL DNI INGRESADO YA EXISTE";
	Class<?>[] groups() default{};
	Class<? extends Payload>[] payload() default {};

}
