package com.ingenieriaweb.springboot.app.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.ingenieriaweb.springboot.app.models.Genero;
import com.ingenieriaweb.springboot.app.models.Usuario;
import com.ingenieriaweb.springboot.app.models.service.IGeneroService;
import com.ingenieriaweb.springboot.app.models.service.IUsuarioService;
import com.ingenieriaweb.springboot.app.paginator.PageRender;
import com.ingenieriaweb.springboot.app.reports.GeneroEXCEL;
import com.ingenieriaweb.springboot.app.reports.GeneroPDF;
import com.ingenieriaweb.springboot.app.reports.UsuarioEXCEL;
import com.ingenieriaweb.springboot.app.reports.UsuarioPDF;
import com.lowagie.text.DocumentException;

@Controller
@RequestMapping("/genero")
@SessionAttributes("genero")
public class GeneroController {
	
	@Autowired
	private IUsuarioService servuser;

	@Autowired
	private IGeneroService serv;
	
	@GetMapping(value = "/generos/{term}", produces = { "application/json" })
	public @ResponseBody List<Genero> cargarRoles(@PathVariable String term) {
		return serv.findByEstadoAndGeneroStartingWith(true,term);
	}
	
	
	@GetMapping("/list")
	public String Listar(@RequestParam(name = "page", defaultValue = "0") int page,@RequestParam(name = "search", defaultValue = "") String search,Model model) {
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());
		model.addAttribute("tituloPagina", "Genero | Listar");
		
		Pageable pageRequest = PageRequest.of(page,10);		
		Page<Genero> generos =serv.findByEstadoAndGeneroStartingWith(true,search,pageRequest);
		PageRender<Genero> pageRender = new PageRender<>("/genero/list", generos);
		model.addAttribute("page", pageRender);
		model.addAttribute("search", search);
		
		model.addAttribute("genero",generos.getContent());
		return "genero/list";
	}
	
	@GetMapping("/create")
	public String create(Model model) {
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());
		model.addAttribute("tituloPagina", "Genero | Crear");
		Genero genero = new Genero();
		model.addAttribute("genero",genero);
		return "genero/create";
	}
	
	@PostMapping("/create")
	public String save(@Valid Genero genero,BindingResult result ,Model model) {
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());
		model.addAttribute("tituloPagina", "Genero | Crear");
		if(result.hasErrors()) {
			return "/genero/create";
		}
		serv.save(genero);
		return "redirect:/genero/list";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable(value="id") int id,Model model) {
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());
		model.addAttribute("tituloPagina", "Genero | Editar");
		model.addAttribute("genero",serv.findById(id));
		model.addAttribute("id",id);
		return "genero/edit";
	}
	
	@PostMapping("/update/{id}")
	public String update(@PathVariable(value="id") int id,@Valid Genero genero) {
		genero.setId_genero(id);
		serv.save(genero);
		return "redirect:/genero/list";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable(value="id") int id,Model model) {
		model.addAttribute("tituloPagina", "Genero | Eliminar");
		serv.findById(id).get().setEstado(false);
		serv.save(serv.findById(id).get());
		return "redirect:/genero/list";

	}
	
	@GetMapping("/generoPDF")
	public void generoPDF(HttpServletResponse response) throws DocumentException, IOException{
		response.setContentType("application/pdf");
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Genero_" + fechaActual + ".pdf";
		
		response.setHeader(cabecera, valor);
		
		List<Genero> tabla = serv.findAll();
		
		GeneroPDF exporter = new GeneroPDF(tabla);
		exporter.exportar(response);
	}
	
	@GetMapping("/generoEXCEL")
	public void generoEXCEL(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/octet-stream");
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Genero_" + fechaActual + ".xlsx";
		
		response.setHeader(cabecera, valor);
		
		List<Genero> tabla = serv.findAll();
		
		GeneroEXCEL exporter = new GeneroEXCEL(tabla);
		exporter.exportar(response);
	}

}
