package com.ingenieriaweb.springboot.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ingenieriaweb.springboot.app.models.Idioma;

public interface IIdiomaService extends JpaRepository<Idioma,Integer> {
	
	public List<Idioma> findByEstado(Boolean estado);
	
	public Page<Idioma> findByEstadoAndIdiomaStartingWith(Boolean estado,String idioma,Pageable pageable);

	public List<Idioma> findByEstadoAndIdiomaStartingWith(Boolean estado,String idioma);
	
	public List<Idioma> findByIdiomaStartingWith(String idioma);


}
