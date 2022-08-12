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

import com.ingenieriaweb.springboot.app.models.Rol;
import com.ingenieriaweb.springboot.app.models.service.IRolService;
import com.ingenieriaweb.springboot.app.models.service.IUsuarioService;
import com.ingenieriaweb.springboot.app.paginator.PageRender;
import com.ingenieriaweb.springboot.app.reports.RolEXCEL;
import com.ingenieriaweb.springboot.app.reports.RolPDF;
import com.lowagie.text.DocumentException;

@Controller
@RequestMapping("/rol")
@SessionAttributes("rol")
public class RolController {
	
	@Autowired
	private IUsuarioService servuser;
	
	@Autowired
	private IRolService serv;
	
	@GetMapping(value = "/roles/{term}", produces = { "application/json" })
	public @ResponseBody List<Rol> cargarRoles(@PathVariable String term) {
		return serv.findByEstadoAndRolStartingWith(true,term);
	}
	
	@GetMapping("/list")
	public String Listar(@RequestParam(name = "page", defaultValue = "0") int page,@RequestParam(name = "search", defaultValue = "") String search,Model model) {
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());
		model.addAttribute("tituloPagina", "Rol | Listar");
		
		Pageable pageRequest = PageRequest.of(page,10);		
		Page<Rol> roles =serv.findByEstadoAndRolStartingWith(true,search,pageRequest);
		PageRender<Rol> pageRender = new PageRender<Rol>("/rol/list", roles);

		model.addAttribute("rol",roles.getContent());		
		model.addAttribute("page", pageRender);
		model.addAttribute("search", search);

		return "rol/list";
	}
	
	@GetMapping("/create")
	public String create(Model model) {
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());
		Rol rol = new Rol();
		model.addAttribute("tituloPagina", "Rol | Crear");
		model.addAttribute("rol",rol);
		return "rol/create";
	}
	
	@PostMapping("/create")
	public String save(@Valid Rol rol,BindingResult result ,Model model) {
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());
		model.addAttribute("tituloPagina", "Rol | Crear");
		if(result.hasErrors()) {
			return "/rol/create";
		}
		serv.save(rol);
		return "redirect:/rol/list";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable(value="id") int id,Model model) {
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());
		model.addAttribute("tituloPagina", "Rol | Editar");
		model.addAttribute("rol",serv.findById(id));
		model.addAttribute("id",id);
		return "rol/edit";
	}
	
	@PostMapping("/update/{id}")
	public String update(@PathVariable(value="id") int id,@Valid Rol rol) {
		rol.setId_rol(id);
		serv.save(rol);
		return "redirect:/rol/list";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable(value="id") int id,Model model) {
		model.addAttribute("tituloPagina", "Rol | Eliminar");
		serv.findById(id).get().setEstado(false);
		serv.save(serv.findById(id).get());
		return "redirect:/rol/list";

	}
	
	@GetMapping("/rolPDF")
	public void rolPDF(HttpServletResponse response) throws DocumentException, IOException{
		response.setContentType("application/pdf");
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Roles_" + fechaActual + ".pdf";
		
		response.setHeader(cabecera, valor);
		
		List<Rol> roles = serv.findAll();
		
		RolPDF exporter = new RolPDF(roles);
		exporter.exportar(response);
	}
	
	@GetMapping("/rolEXCEL")
	public void rolEXCEL(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/octet-stream");
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Roles_" + fechaActual + ".xlsx";
		
		response.setHeader(cabecera, valor);
		
		List<Rol> roles = serv.findAll();
		
		RolEXCEL exporter = new RolEXCEL(roles);
		exporter.exportar(response);
	}
	
}
