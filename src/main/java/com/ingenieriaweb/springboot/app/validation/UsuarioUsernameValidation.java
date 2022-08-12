package com.ingenieriaweb.springboot.app.validation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.ingenieriaweb.springboot.app.models.Usuario;
import com.ingenieriaweb.springboot.app.models.service.IUsuarioService;

public class UsuarioUsernameValidation  implements ConstraintValidator<UsuarioUsername,String>{

	@Autowired
	private IUsuarioService serv;	
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(serv!=null) {
			List<Usuario> model = serv.findAll();
			for(int i=0;i<model.size();i++) {
				if (value.equals(model.get(i).getUsername())){
					return false;
				}
			}	
		}			
		return true;
	}

}
