package com.ingenieriaweb.springboot.app.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;


@Entity
@Table(name="urbanizacion")
public class Urbanizacion {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id_urbanizacion;
	
	@NotEmpty(message = "La urbanizacion no puede ser vacío.")
	private String urbanizacion;
	
	private boolean estado=true;

	public int getId_urbanizacion() {
		return id_urbanizacion;
	}

	public void setId_urbanizacion(int id_urbanizacion) {
		this.id_urbanizacion = id_urbanizacion;
	}

	public String getUrbanizacion() {
		return urbanizacion;
	}

	public void setUrbanizacion(String urbanizacion) {
		this.urbanizacion = urbanizacion;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	

}
