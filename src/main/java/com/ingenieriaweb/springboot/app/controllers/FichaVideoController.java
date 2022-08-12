package com.ingenieriaweb.springboot.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ingenieriaweb.springboot.app.models.Video;
import com.ingenieriaweb.springboot.app.models.service.IVideoService;

@RestController
@RequestMapping("/fichaApi")
public class FichaVideoController {
	
	
	@Autowired
	private IVideoService servvideo;
	
	@GetMapping("/video/{id}")
	public Video cargarVideo(@PathVariable int id) {
		return servvideo.getById(id);
	}
		

}
