package com.ingenieriaweb.springboot.app.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.ingenieriaweb.springboot.app.models.Ficha;
import com.ingenieriaweb.springboot.app.models.FichaVideo;
import com.ingenieriaweb.springboot.app.models.Video;
import com.ingenieriaweb.springboot.app.models.VideoIdioma;
import com.ingenieriaweb.springboot.app.models.service.IClienteService;
import com.ingenieriaweb.springboot.app.models.service.IFichaService;
import com.ingenieriaweb.springboot.app.models.service.IFichaVideoService;
import com.ingenieriaweb.springboot.app.models.service.IUsuarioService;
import com.ingenieriaweb.springboot.app.models.service.IVideoService;
import com.ingenieriaweb.springboot.app.paginator.PageRender;

@Controller
@RequestMapping("/ficha")
@SessionAttributes("ficha")
public class FichaController {
	@Autowired
	private IClienteService servcli;
	
	@Autowired
	private IUsuarioService servuser;
	
	@Autowired
	private IVideoService servvideo;	
	
	@Autowired
	private IFichaService servficha;
	
	@Autowired
	private IFichaVideoService servfichavideo;
	
	/*@Autowired
    private EmailService mailService;*/
	
	
	@GetMapping("/list")
	public String Listar(@RequestParam(name = "page", defaultValue = "0") int page,
					@RequestParam(name = "fechaInicio", defaultValue = "2000-07-07") String fechaInicio,
					@RequestParam(name = "fechaFinal", defaultValue = "2100-09-01") String fechaFinal,
					@RequestParam(name = "fechaInicioA", defaultValue = "2000-07-07") String fechaInicioA,
					@RequestParam(name = "fechaFinalA", defaultValue = "2100-09-01") String fechaFinalA,Model model) {
		
		LocalDate dateFinal =LocalDate.now();
		LocalDate fechaInicioAlquiler= dateFinal.minusMonths(1);
		LocalDate fechaFinalAlquiler=dateFinal;
		LocalDate fechaInicioExpiracion=dateFinal;
		LocalDate fechaFinalExpiracion=dateFinal.plusMonths(1);
		
		if(!fechaInicio.equals("2000-07-07")) {
			fechaInicioExpiracion=LocalDate.parse(fechaInicio);
		}
		
		if(!fechaFinal.equals("2100-09-01")) {
			fechaFinalExpiracion=LocalDate.parse(fechaFinal);
		}
		
		if(!fechaInicioA.equals("2000-07-07")) {
			fechaInicioAlquiler=LocalDate.parse(fechaInicioA);
		}
		
		if(!fechaFinalA.equals("2100-09-01")) {
			fechaFinalAlquiler=LocalDate.parse(fechaFinalA);
		}		
		
		
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());
		model.addAttribute("tituloPagina", "Ficha | Listar");
		
		Pageable pageRequest = PageRequest.of(page,10);		
		Page<Ficha> fichas =servficha.findByClienteAndFechaexpiracionBetweenAndFechaalquilerBetween(servcli.findByDni(loggedInUserName),
				fechaInicioExpiracion,fechaFinalExpiracion,
				fechaInicioAlquiler,fechaFinalAlquiler,pageRequest);		
		PageRender<Ficha> pageRender = new PageRender<Ficha>("/ficha/list", fichas);
		
		
		
		model.addAttribute("page", pageRender);
		
		model.addAttribute("fechaInicio", fechaInicioExpiracion);
		model.addAttribute("fechaFinal",fechaFinalExpiracion);
		
		model.addAttribute("fechaInicioA", fechaInicioAlquiler);
		model.addAttribute("fechaFinalA", fechaFinalAlquiler);
		model.addAttribute("fichas",fichas.getContent());
		return "ficha/list";
	}
	
	@GetMapping("/listficha")
	public String ListarFicha(Model model,@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "fechaInicio", defaultValue = "2000-07-07") String fechaInicio,
			@RequestParam(name = "fechaFinal", defaultValue = "2100-09-01") String fechaFinal,
			@RequestParam(name = "fechaInicioA", defaultValue = "2000-07-07") String fechaInicioA,
			@RequestParam(name = "fechaFinalA", defaultValue = "2100-09-01") String fechaFinalA,
			@RequestParam(name = "buscarClient", defaultValue = "") String buscarClient) {
		
		LocalDate dateFinal =LocalDate.now();
		LocalDate fechaInicioAlquiler= dateFinal.minusMonths(1);
		LocalDate fechaFinalAlquiler=dateFinal;
		LocalDate fechaInicioExpiracion=dateFinal;
		LocalDate fechaFinalExpiracion=dateFinal.plusMonths(1);
		
		if(!fechaInicio.equals("2000-07-07")) {
			fechaInicioExpiracion=LocalDate.parse(fechaInicio);
		}
		
		if(!fechaFinal.equals("2100-09-01")) {
			fechaFinalExpiracion=LocalDate.parse(fechaFinal);
		}
		
		if(!fechaInicioA.equals("2000-07-07")) {
			fechaInicioAlquiler=LocalDate.parse(fechaInicioA);
		}
		
		if(!fechaFinalA.equals("2100-09-01")) {
			fechaFinalAlquiler=LocalDate.parse(fechaFinalA);
		}		
		
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());
		model.addAttribute("tituloPagina", "Ficha | Listar");
		
		Pageable pageRequest = PageRequest.of(page,10);	
		Collection<String> estados= Arrays.asList("2");

		Page<Ficha> fichas =servficha.findByEstadoNotInAndFechaexpiracionBetweenAndFechaalquilerBetweenAndClienteIn(estados,
				fechaInicioExpiracion,fechaFinalExpiracion,
				fechaInicioAlquiler,fechaFinalAlquiler,servcli.findDistinctNombresByEstadoAndNombresStartingWith(true,buscarClient),pageRequest);
		
		PageRender<Ficha> pageRender = new PageRender<Ficha>("/ficha/listficha", fichas);		
		
		model.addAttribute("page", pageRender);		
		model.addAttribute("fechaInicio", fechaInicioExpiracion);
		model.addAttribute("fechaFinal",fechaFinalExpiracion);		
		model.addAttribute("fechaInicioA", fechaInicioAlquiler);
		model.addAttribute("fechaFinalA", fechaFinalAlquiler);
		model.addAttribute("buscarClient", buscarClient);
		model.addAttribute("fichas",fichas.getContent());
		return "ficha/listFicha";
	}
	
	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("tituloPagina", "Ficha | Crear");
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());
		Ficha ficha = new Ficha();
		model.addAttribute("ficha",ficha);
		model.addAttribute("videos",servvideo.findByEstado(true));
		return "ficha/create";
	}
	
	@GetMapping("/createFicha")
	public String createFicha(Model model) {
		model.addAttribute("tituloPagina", "Ficha | Crear");
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());
		Ficha ficha = new Ficha();
		model.addAttribute("ficha",ficha);
		model.addAttribute("videos",servvideo.findByEstado(true));
		return "ficha/createFicha";
	}
	
	@GetMapping(value="/video/{id}", produces = { "application/json" })
	public @ResponseBody Video cargarVideo(@PathVariable int id) {
		return servvideo.getById(id);
	}
	
	@GetMapping(value="/ListProducto", produces = { "application/json" })
	public @ResponseBody List<Video> ListVideos() {
		return servvideo.findByEstadoAndCantidadGreaterThan(true,0);
	}
	
	@GetMapping(value="/ListProducto/{term}", produces = { "application/json" })
	public @ResponseBody List<Video> ListVideosItem(@PathVariable String term) {
		return servvideo.findByEstadoAndCantidadGreaterThanAndTituloContaining(true,0,term);
	}
	
	
	@PostMapping("/create")
	public String datosVideo(String fecha_expiracion,String comentario,
			@RequestParam(name = "id_videos[]", required = false) int[] id_videos,
			@RequestParam(name = "dias[]", required = false) Integer[] cantidad,
			@RequestParam(name = "subtotales[]", required = false) Double[] subtotal,
			@RequestParam(name = "costos[]", required = false) Double[] costos) {
		
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		Double total=0.00;
		Ficha ficha = new Ficha();
		ficha.setCliente(servcli.findByDni(loggedInUserName));
		ficha.setComentario(comentario);
		ficha.setFecha_expiracion(LocalDate.parse(fecha_expiracion));
		List<FichaVideo> itemDetalle = new ArrayList<FichaVideo>();			
		 for(int i=0; i<id_videos.length;i++) { 			 
			 Video idi= servvideo.getById(id_videos[i]);			
			 FichaVideo itemFicha = new FichaVideo(); 			
			 itemFicha.setVideo(idi);
			 itemFicha.setCantidad(cantidad[i]);
			 itemFicha.setCosto(costos[i]);
			 itemFicha.setSubtotal(subtotal[i]);
			 total=total+subtotal[i];			 
			 itemDetalle.add(itemFicha); 		 
		 }	
		ficha.setListVideos(itemDetalle);		
		ficha.setTotal(total);
		servficha.save(ficha);
		return "redirect:/ficha/list";
	}
	

	@PostMapping("/createFicha")
	public String datosVideoFicha(String fecha_expiracion,String comentario,
			@RequestParam(name = "id_videos[]", required = false) int[] id_videos,
			@RequestParam(name = "dias[]", required = false) Integer[] cantidad,
			@RequestParam(name = "subtotales[]", required = false) Double[] subtotal,
			@RequestParam(name = "costos[]", required = false) Double[] costos,int id_cliente) {
		
		Double total=0.00;
		Ficha ficha = new Ficha();
		ficha.setCliente(servcli.getById(id_cliente));
		ficha.setComentario(comentario);
		ficha.setFecha_expiracion(LocalDate.parse(fecha_expiracion));
		List<FichaVideo> itemDetalle = new ArrayList<FichaVideo>();			
		 for(int i=0; i<id_videos.length;i++) { 			 
			 Video idi= servvideo.getById(id_videos[i]);			
			 FichaVideo itemFicha = new FichaVideo(); 			
			 itemFicha.setVideo(idi);
			 itemFicha.setCantidad(cantidad[i]);
			 itemFicha.setCosto(costos[i]);
			 itemFicha.setSubtotal(subtotal[i]);
			 total=total+subtotal[i];			 
			 itemDetalle.add(itemFicha); 		 
		 }	
		ficha.setListVideos(itemDetalle);		
		ficha.setTotal(total);
		servficha.save(ficha);
		return "redirect:/ficha/listficha";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable(value="id") int id,Model model) {	
		for(int i=0;i<servficha.findById(id).get().getListVideos().size();i++) {
			FichaVideo list=servficha.findById(id).get().getListVideos().get(i);
			list.setEstado("2");
			list.setId(list.getId());
			servfichavideo.save(list);
		}		
		servficha.findById(id).get().setEstado("2");
		servficha.save(servficha.findById(id).get());
		return "redirect:/ficha/list";

	}
	
	@GetMapping("/ver/{id}")
	public String ver(@PathVariable(value="id") int id,Model model) {
		model.addAttribute("tituloPagina", "Ficha | Ver");
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());	
		model.addAttribute("ficha",servficha.findById(id).get());
		return "ficha/ver";
	}
	
	@GetMapping("/verficha/{id}")
	public String verficha(@PathVariable(value="id") int id,Model model) {
		model.addAttribute("tituloPagina", "Ficha | Ver");
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());	
		model.addAttribute("ficha",servficha.findById(id).get());
		return "ficha/verFicha";
	}
	
	@GetMapping("/estado/{id}/{id_ficha}")
	public String estado(@PathVariable(value="id") int id,@PathVariable(value="id_ficha") int id_ficha,Model model) {		
		Double mora=0.00,porcentaje=0.10,total=0.00;
		Integer estado=0;
		boolean verificar=false;
		Ficha fich = servficha.getById(id_ficha);
		FichaVideo ficha=servfichavideo.getById(id);
		
		if(LocalDate.now().isAfter(fich.getFecha_expiracion())) {
			long dias = ChronoUnit.DAYS.between(fich.getFecha_expiracion(),LocalDate.now());
			System.out.print(dias);
			mora=(ficha.getCosto()*dias*porcentaje);
		}
		
		if(mora>0.00) {
			ficha.setEstado("5");
		}else {
			ficha.setEstado("4");
		}
		
		ficha.setMora(Math.round(mora*100.0)/100.0);
		ficha.setSubtotal(Math.round((mora+(ficha.getCantidad()*ficha.getCosto()))*100.0)/100.0);
		ficha.setFecha_entrega(LocalDate.now());		
		ficha.setId(ficha.getId());
		servfichavideo.save(ficha);
		
		for(int i=0;i<servficha.findById(id_ficha).get().getListVideos().size();i++) {			
			total=total+servficha.findById(id_ficha).get().getListVideos().get(i).getSubtotal();
			 Video idi= servvideo.getById(servficha.findById(id_ficha).get().getListVideos().get(i).getVideo().getId_video());
			 idi.setId_video(servficha.findById(id_ficha).get().getListVideos().get(i).getVideo().getId_video());
			 idi.setCantidad(idi.getCantidad()+1);
			 servvideo.save(idi);
			if(servficha.findById(id_ficha).get().getListVideos().get(i).getEstado().equals("4")||
					servficha.findById(id_ficha).get().getListVideos().get(i).getEstado().equals("5")) {
				estado=estado+1;
			}
		}
			
		fich.setTotal(Math.round(total*100.0)/100.0);		
		
		if(estado==servficha.findById(id_ficha).get().getListVideos().size()) {
			fich.setEstado("5");
		}else {
			fich.setEstado("4");
		}
		servficha.save(fich);		
		return "redirect:/ficha/verficha/"+id_ficha;
	}
	
	
	@GetMapping("/estadoDFicha/{id}")
	public String estadoDFicha(@PathVariable(value="id") int id,Model model) {		
		for(int i=0;i<servficha.findById(id).get().getListVideos().size();i++) {
			FichaVideo list=servficha.findById(id).get().getListVideos().get(i);
			list.setEstado("1");
			list.setId(list.getId());
			servfichavideo.save(list);
		}		
		servficha.findById(id).get().setEstado("1");
		servficha.save(servficha.findById(id).get());
		return "redirect:/ficha/listficha";
	}
	
	
	@GetMapping("/estadoAFicha/{id}")
	public String estadoAFicha(@PathVariable(value="id") int id,Model model) {		
		for(int i=0;i<servficha.findById(id).get().getListVideos().size();i++) {
			FichaVideo list=servficha.findById(id).get().getListVideos().get(i);
			
			Video idi= list.getVideo();
			idi.setId_video(idi.getId_video());
			idi.setCantidad(idi.getCantidad()-1);
		    servvideo.save(idi);
		    
			list.setEstado("3");
			list.setId(list.getId());
			servfichavideo.save(list);
		}		
		servficha.findById(id).get().setEstado("3");
		servficha.save(servficha.findById(id).get());
		

        /*String message = "\n\n Datos de contacto: nosee";
        mailService.sendMail("sadaquisa5@gmail.com","sadaquisa5@gmail.com","asunto",message);*/		
		
		return "redirect:/ficha/listficha";
	}
	
	
	
	

}
