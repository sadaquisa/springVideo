package com.ingenieriaweb.springboot.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ingenieriaweb.springboot.app.models.Formato;

public interface IFormatoService  extends JpaRepository<Formato,Integer> {
	
	public List<Formato> findByEstado(Boolean estado);
	
	public Page<Formato> findByEstadoAndFormatoStartingWith(Boolean estado,String formato,Pageable pageable);
	
	public List<Formato> findByEstadoAndFormatoStartingWith(Boolean estado,String formato);




}
