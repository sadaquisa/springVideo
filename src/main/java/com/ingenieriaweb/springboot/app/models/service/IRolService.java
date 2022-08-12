package com.ingenieriaweb.springboot.app.models.service;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ingenieriaweb.springboot.app.models.Rol;

public interface IRolService extends JpaRepository<Rol,Integer> {
	
	public List<Rol> findByEstado(Boolean estado);
	
	public List<Rol> findByEstadoAndRolStartingWith(Boolean estado,String rol);
	
	public Page<Rol> findByEstadoAndRolStartingWith(Boolean estado,String rol,Pageable pageable);
	
	public Rol findByRol(String rol);
	
	public List<Rol> findByEstadoAndRolNotIn(Boolean estado,Collection<String> rol);

}
