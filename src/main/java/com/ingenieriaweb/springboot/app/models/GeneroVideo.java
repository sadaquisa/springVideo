package com.ingenieriaweb.springboot.app.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class GeneroVideo {
	
	@JsonIgnoreProperties({"hibernateLazyInitializer"})
	public Genero genero;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer"})
	public List<Video> listVideo;

	public Genero getGenero() {
		return genero;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
	}

	public List<Video> getListVideo() {
		return listVideo;
	}

	public void setListVideo(List<Video> listVideo) {
		this.listVideo = listVideo;
	}
	
}
