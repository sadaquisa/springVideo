package com.ingenieriaweb.springboot.app.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Table(name="ficha")
public class Ficha {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id_ficha;
	
	@Column(name="fecha_alquiler")
	private LocalDate fechaalquiler=LocalDate.now();	

	@Column(name="fecha_expiracion")
	private LocalDate fechaexpiracion;	
	
	private String comentario;
	
	private Double total;
	
	private String estado="0";
	
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_ficha")
	private List<FichaVideo> listVideos;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer"})
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cliente")
	private Cliente cliente;
	
	
	public Ficha() {
		this.listVideos = new ArrayList<FichaVideo>();
	}
	
	public int getId_ficha() {
		return id_ficha;
	}

	public void setId_ficha(int id_ficha) {
		this.id_ficha = id_ficha;
	}

	public LocalDate getFecha_alquiler() {
		return fechaalquiler;
	}

	public void setFecha_alquiler(LocalDate fecha_alquiler) {
		this.fechaalquiler = fecha_alquiler;
	}
	
	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<FichaVideo> getListVideos() {
		return listVideos;
	}

	public void setListVideos(List<FichaVideo> listVideos) {
		this.listVideos = listVideos;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public String getEstado() {
		return estado;
	}

	public LocalDate getFecha_expiracion() {
		return fechaexpiracion;
	}

	public void setFecha_expiracion(LocalDate fecha_expiracion) {
		this.fechaexpiracion = fecha_expiracion;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}	
	
}
