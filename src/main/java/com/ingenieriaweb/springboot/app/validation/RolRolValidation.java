package com.ingenieriaweb.springboot.app.validation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.ingenieriaweb.springboot.app.models.Rol;
import com.ingenieriaweb.springboot.app.models.service.IRolService;

public class RolRolValidation implements ConstraintValidator<RolRol,String>{
	
	@Autowired
	private IRolService servrol;
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {		
		if(servrol!=null) {
			List<Rol> productos = servrol.findAll();
			for(int i=0;i<productos.size();i++) {
				if (value.equals(productos.get(i).getRol())){
					return false;
				}
			}	
		}			
		return true;
	}

}
