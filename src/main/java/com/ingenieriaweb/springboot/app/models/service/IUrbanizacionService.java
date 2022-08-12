package com.ingenieriaweb.springboot.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ingenieriaweb.springboot.app.models.Urbanizacion;

public interface IUrbanizacionService extends JpaRepository<Urbanizacion,Integer> {
	
	public List<Urbanizacion> findByEstado(Boolean estado);
	
	public Page<Urbanizacion> findByEstadoAndUrbanizacionStartingWith(Boolean estado,String urbanizacion,Pageable pageable);

	public List<Urbanizacion> findByEstadoAndUrbanizacionStartingWith(Boolean estado,String urbanizacion);

	public Urbanizacion findByUrbanizacion(String urbanizacion);

}
