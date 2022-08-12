package com.ingenieriaweb.springboot.app.models.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ingenieriaweb.springboot.app.models.Ficha;
import com.ingenieriaweb.springboot.app.models.Reporte;

public interface IReporteService  extends JpaRepository<Ficha,Integer>{

	@Query(value="SELECT v.titulo as nombre, COUNT(V.titulo) AS cantidad FROM ficha_video fv \r\n"
			+ "INNER JOIN video v on fv.id_video=v.id_video GROUP BY v.id_video", nativeQuery=true)
	List<String> VideosCantidad();
	
	@Query(value="SELECT MONTHNAME(f.fecha_alquiler) as nombre , COUNT(V.titulo) AS cantidad FROM ficha_video fv \r\n"
			+ "INNER JOIN video v on fv.id_video=v.id_video inner join ficha f on f.id_ficha=fv.id_ficha\r\n"
			+ "GROUP BY MONTH(f.fecha_alquiler)", nativeQuery=true)
	List<String> VideosCantidadMes();
	
	
	@Query(value="SELECT YEAR(f.fecha_alquiler) as nombre , COUNT(V.titulo) AS cantidad FROM ficha_video fv \r\n"
			+ "INNER JOIN video v on fv.id_video=v.id_video inner join ficha f on f.id_ficha=fv.id_ficha\r\n"
			+ "GROUP BY YEAR(f.fecha_alquiler)", nativeQuery=true)
	List<String> VideosCantidadYear();
	
	@Query(value="SELECT estado, COUNT(estado)as cantidad FROM ficha_video GROUP by estado", nativeQuery=true)
	List<String> CantidadEstados();
	
	@Query(value="SELECT COUNT(V.titulo) AS cantidad FROM ficha_video fv \r\n"
			+ "			INNER JOIN video v on fv.id_video=v.id_video inner join ficha f on f.id_ficha=fv.id_ficha\r\n"
			+ "            GROUP BY MONTH(f.fecha_alquiler)\r\n"
			+ "            ORDER BY cantidad DESC LIMIT 1", nativeQuery=true)
	Integer CantidadVideosMes();
	
	@Query(value="SELECT COUNT(V.titulo) AS cantidad FROM ficha_video fv \r\n"
			+ "			INNER JOIN video v on fv.id_video=v.id_video inner join ficha f on f.id_ficha=fv.id_ficha\r\n"
			+ "            GROUP BY YEAR(f.fecha_alquiler)\r\n"
			+ "            ORDER BY cantidad DESC LIMIT 1", nativeQuery=true)
	Integer CantidadVideosYear();
	
	@Query(value="SELECT COUNT(V.titulo) AS cantidad FROM ficha_video fv\r\n"
			+ "			INNER JOIN video v on fv.id_video=v.id_video \r\n"
			+ "            GROUP BY v.id_video\r\n"
			+ "            ORDER BY cantidad DESC LIMIT 1", nativeQuery=true)
	Integer CantidadVideos();
}
