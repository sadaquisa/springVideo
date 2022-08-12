package com.ingenieriaweb.springboot.app.models.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ingenieriaweb.springboot.app.models.Cliente;
import com.ingenieriaweb.springboot.app.models.Ficha;
import com.ingenieriaweb.springboot.app.models.Rol;

public interface IFichaService extends JpaRepository<Ficha,Integer>{
	
	public List<Ficha> findByEstadoNotIn(Collection<String> estado);
	
	public Page<Ficha> findByClienteAndFechaexpiracionBetweenAndFechaalquilerBetween(Cliente cliente,LocalDate fechaInicio,LocalDate fechaFin,
			LocalDate fechaInicioa,LocalDate fechaFina,Pageable pageable);
	
	public Page<Ficha> findByEstadoNotInAndFechaexpiracionBetweenAndFechaalquilerBetween(Collection<String> estado,
			LocalDate fechaInicio,LocalDate fechaFin,
			LocalDate fechaInicioa,LocalDate fechaFina,Pageable pageable);
	
	public Page<Ficha> findByEstadoNotInAndFechaexpiracionBetweenAndFechaalquilerBetweenAndClienteIn(Collection<String> estado,
			LocalDate fechaInicio,LocalDate fechaFin,
			LocalDate fechaInicioa,LocalDate fechaFina,Collection<Cliente> cliente,Pageable pageable);

}
