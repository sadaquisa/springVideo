package com.ingenieriaweb.springboot.app.models.service;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ingenieriaweb.springboot.app.models.Cliente;
import com.ingenieriaweb.springboot.app.models.Urbanizacion;

public interface IClienteService extends JpaRepository<Cliente,Integer> {
	
	public List<Cliente> findByEstado(Boolean estado);
	
	public Page<Cliente> findByEstadoAndDniStartingWithAndNombresStartingWithAndGeneroStartingWithAndUrbanizacionIn(Boolean estado,String dni,
			String nombres,String genero,Collection<Urbanizacion> urbanizacion,Pageable pageable);

	
	public Cliente findByDni(String dni);
	
	public List<Cliente> findDistinctNombresByEstadoAndNombresStartingWith(Boolean estado,String nombres);

}
