package com.ingenieriaweb.springboot.app.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.ingenieriaweb.springboot.app.models.Usuario;
import com.ingenieriaweb.springboot.app.models.Video;
import com.ingenieriaweb.springboot.app.models.service.IClienteService;
import com.ingenieriaweb.springboot.app.models.service.IGeneroService;
import com.ingenieriaweb.springboot.app.models.service.IRolService;
import com.ingenieriaweb.springboot.app.models.service.IUsuarioService;
import com.ingenieriaweb.springboot.app.models.service.IVideoService;

@Controller
@RequestMapping("/")
@SessionAttributes("index")
public class IndexController {
	
	@Autowired
	private IUsuarioService serv;
	
	@Autowired
	private IVideoService servVid;
	
	@Autowired
	private IRolService servRol;
	
	@Autowired
	private IGeneroService servgen;
	
	@GetMapping({"/","/home"})
	public String index(Model model) {
		model.addAttribute("tituloPagina", "Inicio");
		model.addAttribute("titulo","Inicio");
		return "index";
	}
	
	@GetMapping({"/login"})
	public String login(Model model) {
		model.addAttribute("tituloPagina", "Login");
		model.addAttribute("titulo","Login");
		return "login";
	}	
	
	@GetMapping({"/dashboard"})
	public String dashboard(Model model) {
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		
		if(serv.findByUsername(loggedInUserName).getRol().getRol().equals("CLIENTE")) {
			List<Video> listVideo = new ArrayList<Video>();
			for(int j=0; j<servVid.Top10Videos().size();j++) {			
				  String[] part  = servVid.Top10Videos().get(j).split(",");
				  Video vid = servVid.getById(Integer.parseInt(part[0]));
				  listVideo.add(vid);
			}
			model.addAttribute("top10videos",listVideo);
			
			List<Video> listGeneroAccion = new ArrayList<Video>();
			String replace3 = servVid.GeneroVideo("ACCION").toString().replace("[","");
			String replace4 = replace3.replace("]","");
			String [] vect1 = replace4.split(", ");
			if(!vect1[0].toString().equals("")) {
				for(int j=0; j<vect1.length;j++) {			
					  Video vid = servVid.getById(Integer.parseInt(vect1[j]));
					  listGeneroAccion.add(vid);
				}
			}
			model.addAttribute("listGeneroAccion",listGeneroAccion);
			
			List<Video> listGeneroCiencia = new ArrayList<Video>();		
			String replace5 = servVid.GeneroVideo("CIENCIA FICCIÓN").toString().replace("[","");
			String replace6= replace5.replace("]","");
			String [] vect2 = replace6.split(", ");	
			if(!vect2[0].toString().equals("")) {
				for(int j=0; j<vect2.length;j++) {			
					  Video vid = servVid.getById(Integer.parseInt(vect2[j]));
					  listGeneroCiencia.add(vid);
				}
			}
			model.addAttribute("listGeneroCiencia",listGeneroCiencia);
			
			List<Video> listGeneroAnimacion = new ArrayList<Video>();
			String replace9 = servVid.GeneroVideo("ANIMACIÓN").toString().replace("[","");
			String replace10= replace9.replace("]","");
			String [] vect4 = replace10.split(", ");	
			if(!vect4[0].toString().equals("")) {
				for(int j=0; j<vect4.length;j++) {			
					  Video vid = servVid.getById(Integer.parseInt(vect4[j]));
					  listGeneroAnimacion.add(vid);
				}
			}
			model.addAttribute("listGeneroAnimacion",listGeneroAnimacion);
			
			
			List<Video> listGeneroTerror = new ArrayList<Video>();
			String replace1 = servVid.GeneroVideo("TERROR").toString().replace("[","");
			String replace2 = replace1.replace("]","");
			String [] vect = replace2.split(", ");	
			if(!vect[0].toString().equals("")) {
				for(int j=0; j<vect.length;j++) {			
					  Video vid = servVid.getById(Integer.parseInt(vect[j]));
					  listGeneroTerror.add(vid);
				}
			}
			model.addAttribute("listGeneroTerror",listGeneroTerror);
			
			
			List<Video> listGeneroDeporte = new ArrayList<Video>();
			String replace7 = servVid.GeneroVideo("DEPORTES").toString().replace("[","");
			String replace8= replace7.replace("]","");
			String [] vect3 = replace8.split(", ");
			if(!vect3[0].toString().equals("")) {
				for(int j=0; j<vect3.length;j++) {			
					  Video vid = servVid.getById(Integer.parseInt(vect3[j]));
					  listGeneroDeporte.add(vid);
				}
			}			
			model.addAttribute("listGeneroDeporte",listGeneroDeporte);		
		}
		
		model.addAttribute("top",1);
		model.addAttribute("usuario_autenticado", serv.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", serv.findByUsername(loggedInUserName).getRol().getRol());
		model.addAttribute("tituloPagina", "Dashboard");
		model.addAttribute("titulo","Dashboard");
		return "dashboard";
	}
}
