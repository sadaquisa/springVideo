package com.ingenieriaweb.springboot.app.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.ingenieriaweb.springboot.app.validation.RolRol;

@Entity
@Table(name="rol")
public class Rol {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id_rol;
	
	@NotEmpty(message = "EL ROL NO PUEDE SER VACIO")
	@RolRol
	private String rol;
	
	private boolean estado=true;

	public int getId_rol() {
		return id_rol;
	}

	public void setId_rol(int id_rol) {
		this.id_rol = id_rol;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public boolean getEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	

}
