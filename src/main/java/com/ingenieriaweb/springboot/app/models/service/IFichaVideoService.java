package com.ingenieriaweb.springboot.app.models.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ingenieriaweb.springboot.app.models.FichaVideo;

public interface IFichaVideoService extends JpaRepository<FichaVideo,Integer>{

}
