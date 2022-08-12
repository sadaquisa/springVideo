package com.ingenieriaweb.springboot.app.models.service;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ingenieriaweb.springboot.app.models.Formato;
import com.ingenieriaweb.springboot.app.models.Video;

public interface IVideoService extends JpaRepository<Video,Integer>{
	
	public List<Video> findByEstado(Boolean estado);
	
	public List<Video> findByEstadoAndCantidadGreaterThan(Boolean estado,Integer cantidad);

	public List<Video> findByEstadoAndCantidadGreaterThanAndTituloContaining(Boolean estado,Integer cantidad,String titulo);

	public Page<Video> findByEstadoAndTituloContainingAndAnioStartingWithAndFormatoIn(Boolean estado,String titulo,
			String anio,Collection<Formato> formato,Pageable pageable);
	
	
	public List<Video> findByEstadoAndTituloStartingWithAndAnioStartingWithAndFormatoIn(Boolean estado,String titulo,
			String anio,Collection<Formato> formato);
	
	@Query(value="SELECT v.id_video FROM ficha_video fv \r\n"
			+ "INNER JOIN video v on fv.id_video=v.id_video \r\n"
			+ "GROUP BY  v.titulo\r\n"
			+ "ORDER BY COUNT(v.titulo) DESC\r\n"
			+ "LIMIT 10", nativeQuery=true)
	List<String> Top10Videos();
	
	@Query(value="SELECT v.id_video FROM ficha_video fv INNER JOIN video v on fv.id_video=v.id_video \r\n"
			+ "			INNER JOIN video_genero vg ON vg.id_video=v.id_video\r\n"
			+ "            INNER JOIN genero g ON g.id_genero=vg.id_genero\r\n"
			+ "            WHERE g.genero= :genero\r\n"
			+ "			GROUP BY  v.titulo\r\n"
			+ "			ORDER BY rand() \r\n"
			+ "			LIMIT 10", nativeQuery=true)
	List<String> GeneroVideo(String genero);
	
	@Query(value="SELECT id_genero FROM genero", nativeQuery=true)
	List<String> Genero();

}
