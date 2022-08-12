package com.ingenieriaweb.springboot.app.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name="formato")
public class Formato {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id_formato;
	
	@NotEmpty(message = "El formato no puede ser vacío.")
	private String formato;
	
	private boolean estado=true;
	
	@OneToMany(mappedBy="formato")
	private List<Video> video;

	public int getId_formato() {
		return id_formato;
	}

	public void setId_formato(int id_formato) {
		this.id_formato = id_formato;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	

}
