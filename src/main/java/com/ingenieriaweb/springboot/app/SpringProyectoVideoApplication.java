package com.ingenieriaweb.springboot.app;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import com.ingenieriaweb.springboot.app.controllers.UsuarioController;
import com.ingenieriaweb.springboot.app.controllers.VideoController;

@SpringBootApplication
public class SpringProyectoVideoApplication {
	public static void main(String[] args) {
		new File(UsuarioController.uploadDirectory).mkdir();
		new File(VideoController.uploadDirectoryVideo).mkdir();
		SpringApplication.run(SpringProyectoVideoApplication.class, args);
	}

}
