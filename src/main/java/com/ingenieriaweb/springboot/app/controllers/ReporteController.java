package com.ingenieriaweb.springboot.app.controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.ingenieriaweb.springboot.app.models.Reporte;
import com.ingenieriaweb.springboot.app.models.Video;
import com.ingenieriaweb.springboot.app.models.service.IReporteService;
import com.ingenieriaweb.springboot.app.models.service.IUsuarioService;
import com.ingenieriaweb.springboot.app.models.service.IVideoService;


@Controller
@RequestMapping("/reporte")
@SessionAttributes("reporte")
public class ReporteController {
	@Autowired
	private IUsuarioService servuser;
	
	@Autowired
	private IVideoService serv;
	

	@Autowired
	private IReporteService servRep;
	
	@GetMapping("/videocantidad")
	public String grafico1(Model model) {
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());
		model.addAttribute("tituloPagina", "Reporte | Videos");
		
		Map<String, Integer> data = new LinkedHashMap<String, Integer>();
		
	
		for(int j=0; j<servRep.VideosCantidad().size();j++) {			
			  String[] part  = servRep.VideosCantidad().get(j).split(",") ;
			  data.put(part[0],Integer.parseInt(part[1]));			  
		}	
		
		model.addAttribute("keySet", data.keySet());
		model.addAttribute("values", data.values());
		
		data = new LinkedHashMap<String, Integer>();

		
		for(int j=0; j<servRep.VideosCantidadMes().size();j++) {			
			  String[] part  = servRep.VideosCantidadMes().get(j).split(",") ;
			  data.put(part[0],Integer.parseInt(part[1]));			  
		}	
		
		model.addAttribute("keySet1", data.keySet());
		model.addAttribute("values1", data.values());
		
		
		data = new LinkedHashMap<String, Integer>();

		
		for(int j=0; j<servRep.VideosCantidadYear().size();j++) {			
			  String[] part  = servRep.VideosCantidadYear().get(j).split(",") ;
			  data.put(part[0],Integer.parseInt(part[1]));			  
		}	
		
		model.addAttribute("keySet2", data.keySet());
		model.addAttribute("values2", data.values());		
		
		
		model.addAttribute("cantidadVideosMes",servRep.CantidadVideosMes());
		
		model.addAttribute("cantidadVideosYear",servRep.CantidadVideosYear());	

		model.addAttribute("cantidadVideos",servRep.CantidadVideos());	

		/* GRAFICOOOOOOOOOOOOOOOOOOOOOOO DE PIEEEEEEEEEEEEEEEEEEEE */
		
		double[] porcentaje = new double[] {0.00,0.00,0.00,0.00,0.00,0.00};		
		for(int i=0;i<6;i++) {
			for(int j=0;j<servRep.CantidadEstados().size();j++) {
				String[] part  = servRep.CantidadEstados().get(j).split(",");
				if(part[0].equals(String.valueOf(i))) {
					porcentaje[i]=Double.parseDouble(part[1]);
					j=servRep.CantidadEstados().size();
				}else {
					porcentaje[i]=0.00;
				}
			}
			
		}
		/* System.out.print(servRep.CantidadEstados())*/;

		model.addAttribute("enespera",porcentaje[0]);	
		model.addAttribute("denegado",porcentaje[1]);
		model.addAttribute("anulado",porcentaje[2]);
		model.addAttribute("pendiente",porcentaje[3]);	
		model.addAttribute("devueltotiempo",porcentaje[4]);
		model.addAttribute("devueltodestiempo",porcentaje[5]); 
		
		System.out.print(porcentaje[4]);

		return "reportes/grafico1";
	}
	

	@GetMapping(value = "/datosVideos", produces = { "application/json" })
	public  @ResponseBody List<Video> graficoDatos() {
		return serv.findAll();
	}
	

}
