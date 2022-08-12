package com.ingenieriaweb.springboot.app.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;


@Entity
@Table(name="genero")
public class Genero {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id_genero;
	
	@NotEmpty(message = "El genero no puede ser vacío.")
	private String genero;
	
	private boolean estado=true;

	public int getId_genero() {
		return id_genero;
	}

	public void setId_genero(int id_genero) {
		this.id_genero = id_genero;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
}
