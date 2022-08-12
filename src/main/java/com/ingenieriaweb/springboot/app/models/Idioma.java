package com.ingenieriaweb.springboot.app.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name="idioma")
public class Idioma {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id_idioma;
	
	@NotEmpty(message = "El idioma no puede ser vacío.")
	private String idioma;
	
	private boolean estado=true;

	public int getId_idioma() {
		return id_idioma;
	}

	public void setId_idioma(int id_idioma) {
		this.id_idioma = id_idioma;
	}

	public String getIdioma() {
		return idioma;
	}

	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}	

}
