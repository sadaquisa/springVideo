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

@Entity
@Table(name="video")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Video {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id_video;
	
	@NotEmpty(message = "El titulo no puede ser vacío.")
	private String titulo;
	
	@NotEmpty(message = "El año no puede ser vacío.")
	@Size(min=4,max=4, message="El año debe ser de 4 numeros")
	private String anio;
	
	@NotNull(message = "La cantidad no puede ser vacío.")
	private int cantidad;
	
	private String portada;
	
	private double precio;	
	
	private boolean estado=true;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer"})
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="id_formato")
	private Formato formato;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer"})
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_video")
	private List<VideoGenero> list_video_genero;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer"})
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_video")
	private List<VideoIdioma> list_video_idioma;
	
	
	public Video() {
		this.list_video_genero = new ArrayList<VideoGenero>();
		this.list_video_idioma = new ArrayList<VideoIdioma>();
	}

	
	public int getId_video() {
		return id_video;
	}

	public void setId_video(int id_video) {
		this.id_video = id_video;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAnio() {
		return anio;
	}

	public void setAnio(String año) {
		this.anio = año;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getPortada() {
		return portada;
	}

	public void setPortada(String portada) {
		this.portada = portada;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public Formato getFormato() {
		return formato;
	}

	public void setFormato(Formato formato) {
		this.formato = formato;
	}
	
	public void addVideoGenero(VideoGenero item) {
		this.list_video_genero.add(item);
	}
	
	public void addVideoIdioma(VideoIdioma item) {
		this.list_video_idioma.add(item);
	}


	public List<VideoGenero> getList_video_genero() {
		return list_video_genero;
	}


	public void setList_video_genero(List<VideoGenero> list_video_genero) {
		this.list_video_genero = list_video_genero;
	}


	public List<VideoIdioma> getList_video_idioma() {
		return list_video_idioma;
	}


	public void setList_video_idioma(List<VideoIdioma> list_video_idioma) {
		this.list_video_idioma = list_video_idioma;
	}


	public double getPrecio() {
		return precio;
	}


	public void setPrecio(double precio) {
		this.precio = precio;
	}
	
	
}
