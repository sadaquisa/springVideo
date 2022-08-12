package com.ingenieriaweb.springboot.app.models;

public class Reporte {
	
	public String nombre;
	
	public Integer cantidad;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}	

}
