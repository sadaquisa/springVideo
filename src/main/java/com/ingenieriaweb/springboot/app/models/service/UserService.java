package com.ingenieriaweb.springboot.app.models.service;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ingenieriaweb.springboot.app.models.Usuario;

@Service
public class UserService implements UserDetailsService{
	
	
	@Autowired
	private IUsuarioService servUsu;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario us=servUsu.findByUsername(username);
		if(us==null) {
			throw new UsernameNotFoundException("Usuario o password inválidos");
		}		
		List<GrantedAuthority> roles = new ArrayList<>();
		roles.add(new SimpleGrantedAuthority("ADMIN"));
		UserDetails userDet = new User(us.getUsername(),us.getPassword(),roles);		
		return userDet;
		
	}

}
