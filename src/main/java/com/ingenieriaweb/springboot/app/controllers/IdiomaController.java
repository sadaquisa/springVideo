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

import com.ingenieriaweb.springboot.app.models.Idioma;
import com.ingenieriaweb.springboot.app.models.Usuario;
import com.ingenieriaweb.springboot.app.models.service.IIdiomaService;
import com.ingenieriaweb.springboot.app.models.service.IUsuarioService;
import com.ingenieriaweb.springboot.app.paginator.PageRender;
import com.ingenieriaweb.springboot.app.reports.IdiomaEXCEL;
import com.ingenieriaweb.springboot.app.reports.IdiomaPDF;
import com.ingenieriaweb.springboot.app.reports.UsuarioEXCEL;
import com.ingenieriaweb.springboot.app.reports.UsuarioPDF;
import com.lowagie.text.DocumentException;

@Controller
@RequestMapping("/idioma")
@SessionAttributes("idioma")
public class IdiomaController {
	
	@Autowired
	private IUsuarioService servuser;

	@Autowired
	private IIdiomaService serv;
	
	@GetMapping(value = "/idiomas/{term}", produces = { "application/json" })
	public @ResponseBody List<Idioma> cargarRoles(@PathVariable String term) {
		return serv.findByEstadoAndIdiomaStartingWith(true,term);
	}
	
	
	@GetMapping("/list")
	public String Listar(@RequestParam(name = "page", defaultValue = "0") int page,@RequestParam(name = "search", defaultValue = "") String search,Model model) {
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());
		model.addAttribute("tituloPagina", "Idioma | Listar");
		
		Pageable pageRequest = PageRequest.of(page,10);		
		Page<Idioma> idiomas =serv.findByEstadoAndIdiomaStartingWith(true,search,pageRequest);
		PageRender<Idioma> pageRender = new PageRender<>("/idioma/list", idiomas);
		model.addAttribute("page", pageRender);
		model.addAttribute("search", search);
		
		model.addAttribute("idioma",idiomas.getContent());
		return "idioma/list";
	}
	
	@GetMapping("/create")
	public String create(Model model) {
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());
		model.addAttribute("tituloPagina", "Idioma | Crear");
		Idioma idioma = new Idioma();
		model.addAttribute("idioma",idioma);
		return "idioma/create";
	}
	
	@PostMapping("/create")
	public String save(@Valid Idioma idioma,BindingResult result ,Model model) {
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());
		model.addAttribute("tituloPagina", "Genero | Crear");
		if(result.hasErrors()) {
			return "/idioma/create";
		}
		serv.save(idioma);
		return "redirect:/idioma/list";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable(value="id") int id,Model model) {
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());
		model.addAttribute("tituloPagina", "Genero | Editar");
		model.addAttribute("idioma",serv.findById(id));
		model.addAttribute("id",id);
		return "idioma/edit";
	}
	
	@PostMapping("/update/{id}")
	public String update(@PathVariable(value="id") int id,@Valid Idioma idioma) {
		idioma.setId_idioma(id);
		serv.save(idioma);
		return "redirect:/idioma/list";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable(value="id") int id,Model model) {
		serv.findById(id).get().setEstado(false);
		serv.save(serv.findById(id).get());
		return "redirect:/idioma/list";

	}
	
	@GetMapping("/idiomaPDF")
	public void idiomaPDF(HttpServletResponse response) throws DocumentException, IOException{
		response.setContentType("application/pdf");
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Idioma_" + fechaActual + ".pdf";
		
		response.setHeader(cabecera, valor);
		
		List<Idioma> tabla = serv.findAll();
		
		IdiomaPDF exporter = new IdiomaPDF(tabla);
		exporter.exportar(response);
	}
	
	@GetMapping("/idiomaEXCEL")
	public void idiomaEXCEL(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/octet-stream");
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Idioma_" + fechaActual + ".xlsx";
		
		response.setHeader(cabecera, valor);
		
		List<Idioma> tabla = serv.findAll();
		
		IdiomaEXCEL exporter = new IdiomaEXCEL(tabla);
		exporter.exportar(response);
	}
}
