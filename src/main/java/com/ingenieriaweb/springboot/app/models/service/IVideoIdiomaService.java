package com.ingenieriaweb.springboot.app.models.service;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ingenieriaweb.springboot.app.models.Idioma;
import com.ingenieriaweb.springboot.app.models.VideoIdioma;

public interface IVideoIdiomaService extends JpaRepository<VideoIdioma,Integer>{
	
	@Modifying
	@Transactional
	@Query(value="Delete from video_idioma WHERE id_video=:id", nativeQuery=true)
	void EliminarDetalle(@Param("id") int id);
	
	public VideoIdioma findByIdiomaIn(Collection<Idioma> idioma);


}
