package com.ingenieriaweb.springboot.app.validation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.ingenieriaweb.springboot.app.models.Cliente;
import com.ingenieriaweb.springboot.app.models.service.IClienteService;

public class ClienteDniValidation  implements ConstraintValidator<ClienteDni,String>{
	
	@Autowired
	private IClienteService serv;
	

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {		
		if(serv!=null) {
			List<Cliente> model = serv.findAll();
			for(int i=0;i<model.size();i++) {
				if (value.equals(model.get(i).getDni())){
					return false;
				}
			}	
		}			
		return true;
	}

}
