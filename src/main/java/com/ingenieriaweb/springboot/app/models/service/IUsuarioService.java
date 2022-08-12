package com.ingenieriaweb.springboot.app.models.service;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ingenieriaweb.springboot.app.models.Rol;
import com.ingenieriaweb.springboot.app.models.Usuario;

public interface IUsuarioService extends JpaRepository<Usuario,Integer>{

	public List<Usuario> findByEstado(Boolean estado);
	
	public Page<Usuario> findByEstadoAndUsernameStartingWithAndNombresStartingWithAndRolIn(Boolean estado,String username,
			String nombres,Collection<Rol> rol,Pageable pageable);

	
	public Usuario findByUsername(String username);
}
