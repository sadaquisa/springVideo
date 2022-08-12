package com.ingenieriaweb.springboot.app.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ingenieriaweb.springboot.app.validation.ClienteDni;

@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Table(name="cliente")
public class Cliente {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id_cliente;
	
	@NotEmpty(message = "El dni no puede ser vacío.")
	@ClienteDni
	@Size(min = 8, max = 8, message="El DNI debe tener 8 numeros")
	private String dni;
	
	@NotEmpty(message = "Los nombres no puede ser vacío.")
	private String nombres;
	
	@NotEmpty(message = "Debe seleccionar un genero.")
	private String genero;
	
	@NotNull(message = "La edad no puede ser vacío.")
	private int edad;
	
	@NotEmpty(message = "La direccion no puede ser vacío.")
	private String direccion;
	
	@NotEmpty(message = "El telefono no puede ser vacío.")
	@Size(max = 10, message="El TELEFONO debe tener como maximo 10 numeros")
	private String telefono;
	
	private String foto;
	
	private boolean estado=true;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer"})
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="id_urbanizacion")
	private Urbanizacion urbanizacion;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer"})
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_cliente")
	private List<Ficha> list_ficha;

	public Cliente() {
		this.list_ficha = new ArrayList<Ficha>();
	}
	

	public List<Ficha> getList_ficha() {
		return list_ficha;
	}

	public void setList_ficha(List<Ficha> list_ficha) {
		this.list_ficha = list_ficha;
	}

	public int getId_cliente() {
		return id_cliente;
	}


	public void setId_cliente(int id_cliente) {
		this.id_cliente = id_cliente;
	}


	public String getDni() {
		return dni;
	}


	public void setDni(String dni) {
		this.dni = dni;
	}


	public String getNombres() {
		return nombres;
	}


	public void setNombres(String nombres) {
		this.nombres = nombres;
	}


	public String getGenero() {
		return genero;
	}


	public void setGenero(String genero) {
		this.genero = genero;
	}


	public int getEdad() {
		return edad;
	}


	public void setEdad(int edad) {
		this.edad = edad;
	}


	public String getDireccion() {
		return direccion;
	}


	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}


	public String getTelefono() {
		return telefono;
	}


	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}


	public String getFoto() {
		return foto;
	}


	public void setFoto(String foto) {
		this.foto = foto;
	}


	public boolean isEstado() {
		return estado;
	}


	public void setEstado(boolean estado) {
		this.estado = estado;
	}


	public Urbanizacion getUrbanizacion() {
		return urbanizacion;
	}


	public void setUrbanizacion(Urbanizacion urbanizacion) {
		this.urbanizacion = urbanizacion;
	}	

}
