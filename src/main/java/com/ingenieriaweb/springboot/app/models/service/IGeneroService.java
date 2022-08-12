package com.ingenieriaweb.springboot.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ingenieriaweb.springboot.app.models.Genero;

public interface IGeneroService extends JpaRepository<Genero,Integer> {
	
	public List<Genero> findByEstado(Boolean estado);
	
	public Page<Genero> findByEstadoAndGeneroStartingWith(Boolean estado,String genero,Pageable pageable);

	public List<Genero> findByEstadoAndGeneroStartingWith(Boolean estado,String genero);
	
	public List<Genero> findByGeneroStartingWith(String genero);



}
