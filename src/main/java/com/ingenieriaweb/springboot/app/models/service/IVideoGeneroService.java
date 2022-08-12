package com.ingenieriaweb.springboot.app.models.service;


import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ingenieriaweb.springboot.app.models.Genero;
import com.ingenieriaweb.springboot.app.models.VideoGenero;

public interface IVideoGeneroService extends JpaRepository<VideoGenero,Integer>{
	
	@Modifying
	@Transactional
	@Query(value="Delete from video_genero WHERE id_video=:id", nativeQuery=true)
	void EliminarDetalle(@Param("id") int id);
	
	
	public VideoGenero findByGeneroIn(Collection<Genero> genero);
	

}
