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

import com.ingenieriaweb.springboot.app.models.Formato;
import com.ingenieriaweb.springboot.app.models.Usuario;
import com.ingenieriaweb.springboot.app.models.service.IClienteService;
import com.ingenieriaweb.springboot.app.models.service.IFormatoService;
import com.ingenieriaweb.springboot.app.models.service.IUsuarioService;
import com.ingenieriaweb.springboot.app.paginator.PageRender;
import com.ingenieriaweb.springboot.app.reports.FormatoEXCEL;
import com.ingenieriaweb.springboot.app.reports.FormatoPDF;
import com.ingenieriaweb.springboot.app.reports.UsuarioEXCEL;
import com.ingenieriaweb.springboot.app.reports.UsuarioPDF;
import com.lowagie.text.DocumentException;


@Controller
@RequestMapping("/formato")
@SessionAttributes("formato")
public class FormatoController {
	
	@Autowired
	private IUsuarioService servuser;
	
	@Autowired
	private IFormatoService serv;
	
	@GetMapping(value = "/formatos/{term}", produces = { "application/json" })
	public @ResponseBody List<Formato> cargarRoles(@PathVariable String term) {
		return serv.findByEstadoAndFormatoStartingWith(true,term);
	}
	
	
	@GetMapping("/list")
	public String Listar(@RequestParam(name = "page", defaultValue = "0") int page,@RequestParam(name = "search", defaultValue = "") String search,Model model) {
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());
		model.addAttribute("tituloPagina", "Formato | Listar");
		
		Pageable pageRequest = PageRequest.of(page,10);		
		Page<Formato> formatos =serv.findByEstadoAndFormatoStartingWith(true,search,pageRequest);
		PageRender<Formato> pageRender = new PageRender<Formato>("/formato/list", formatos);
		model.addAttribute("page", pageRender);
		model.addAttribute("search", search);		
		model.addAttribute("formato",formatos.getContent());
		return "formato/list";
	}
	
	@GetMapping("/create")
	public String create(Model model) {
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());
		model.addAttribute("tituloPagina", "Formato | Crear");
		Formato formato = new Formato();
		model.addAttribute("formato",formato);
		return "formato/create";
	}
	
	@PostMapping("/create")
	public String save(@Valid Formato formato,BindingResult result ,Model model) {
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());
		model.addAttribute("tituloPagina", "Formato | Crear");
		if(result.hasErrors()) {
			return "/formato/create";
		}
		serv.save(formato);
		return "redirect:/formato/list";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable(value="id") int id,Model model) {
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());
		model.addAttribute("tituloPagina", "Formato | Editar");
		model.addAttribute("formato",serv.findById(id));
		model.addAttribute("id",id);
		return "formato/edit";
	}
	
	@PostMapping("/update/{id}")
	public String update(@PathVariable(value="id") int id,@Valid Formato formato) {
		formato.setId_formato(id);
		serv.save(formato);
		return "redirect:/formato/list";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable(value="id") int id,Model model) {
		model.addAttribute("tituloPagina", "Formato | Editar");
		serv.findById(id).get().setEstado(false);
		serv.save(serv.findById(id).get());
		return "redirect:/formato/list";

	}
	
	@GetMapping("/formatoPDF")
	public void formatoPDF(HttpServletResponse response) throws DocumentException, IOException{
		response.setContentType("application/pdf");
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Formato_" + fechaActual + ".pdf";
		
		response.setHeader(cabecera, valor);
		
		List<Formato> tabla = serv.findAll();
		
		FormatoPDF exporter = new FormatoPDF(tabla);
		exporter.exportar(response);
	}
	
	@GetMapping("/formatoEXCEL")
	public void formatoEXCEL(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/octet-stream");
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Formato_" + fechaActual + ".xlsx";
		
		response.setHeader(cabecera, valor);
		
		List<Formato> tabla = serv.findAll();
		
		FormatoEXCEL exporter = new FormatoEXCEL(tabla);
		exporter.exportar(response);
	}

}
