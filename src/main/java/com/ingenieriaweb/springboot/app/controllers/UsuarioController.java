package com.ingenieriaweb.springboot.app.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.ingenieriaweb.springboot.app.models.Cliente;
import com.ingenieriaweb.springboot.app.models.Rol;
import com.ingenieriaweb.springboot.app.models.Usuario;
import com.ingenieriaweb.springboot.app.models.service.IUsuarioService;
import com.ingenieriaweb.springboot.app.paginator.PageRender;
import com.ingenieriaweb.springboot.app.reports.RolEXCEL;
import com.ingenieriaweb.springboot.app.reports.RolPDF;
import com.ingenieriaweb.springboot.app.reports.UsuarioEXCEL;
import com.ingenieriaweb.springboot.app.reports.UsuarioPDF;
import com.lowagie.text.DocumentException;
import com.ingenieriaweb.springboot.app.models.service.IClienteService;
import com.ingenieriaweb.springboot.app.models.service.IRolService;

@Controller
@RequestMapping("/usuario")
@SessionAttributes("usuario")
public class UsuarioController {
	
	@Autowired
	private IUsuarioService serv;
		
	@Autowired
	private IRolService servrol;	
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	@Autowired
	private IClienteService servCli;
	
	public static String uploadDirectory= System.getProperty("user.dir")+"/src/main/webapp/foto_cliente";
	
	@GetMapping("/list")
	public String Listar(@RequestParam(name = "page", defaultValue = "0") int page
			,@RequestParam(name = "username", defaultValue = "") String username
			,@RequestParam(name = "nombres", defaultValue = "") String nombres
			,@RequestParam(name = "buscarRol", defaultValue = "") String buscarRol
			,Model model) {
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", serv.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", serv.findByUsername(loggedInUserName).getRol().getRol());
		model.addAttribute("tituloPagina", "Usuario | Listar");
		
		
		Pageable pageRequest = PageRequest.of(page,10);		
		Page<Usuario> usuarios =serv.findByEstadoAndUsernameStartingWithAndNombresStartingWithAndRolIn(true,username,nombres,
				servrol.findByEstadoAndRolStartingWith(true, buscarRol),pageRequest);
		PageRender<Usuario> pageRender = new PageRender<Usuario>("/usuario/list", usuarios);
		model.addAttribute("page", pageRender);
		model.addAttribute("username", username);	
		model.addAttribute("nombres", nombres);	
		model.addAttribute("buscarRol", buscarRol);
		
		model.addAttribute("usuario",usuarios.getContent());
		return "usuario/list";
	}
	
	@GetMapping("/perfil")
	public String Perfil(Model model) {
		int id_cliente=0;
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", serv.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", serv.findByUsername(loggedInUserName).getRol().getRol());
		if(servCli.findByDni(loggedInUserName)!=null) {
			id_cliente=servCli.findByDni(loggedInUserName).getId_cliente();
		}
		
		model.addAttribute("id_cliente",id_cliente);
		model.addAttribute("usuario", serv.findByUsername(loggedInUserName));
		model.addAttribute("tituloPagina", "Peril | "+loggedInUserName);
		return "usuario/perfil";
	}
	
	@PostMapping("/perfil_update/{id}")
	public String PerfilUpdate(@PathVariable(value="id") int id,Usuario usuario,@RequestParam("fotoinput") MultipartFile foto,
			String password_final) {
		String uniqueFilename="";
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		
		
		if(!foto.getOriginalFilename().equals("")) {
			if (!foto.isEmpty()) {		
				Path directorio =Paths.get(uploadDirectory);
				uniqueFilename = UUID.randomUUID().toString() + "FOTO-USUARIO" + foto.getOriginalFilename();
				String rutaAbsoluta=directorio.toFile().getAbsolutePath();
				try {
					byte[] bytesImg=foto.getBytes();
					Path rutaCompleta=Paths.get(rutaAbsoluta+"//"+uniqueFilename);
					Files.write(rutaCompleta, bytesImg);				
				} catch (IOException e) {
					e.printStackTrace();
				}		
			}else {
				uniqueFilename="perfil_anonimo.png";			
			}			
			usuario.setFoto(uniqueFilename);
		}
		
		if(serv.findByUsername(loggedInUserName).getRol().getRol().equals("CLIENTE")) {
			Cliente cli =servCli.findById(servCli.findByDni(loggedInUserName).getId_cliente()).get();
			if(!foto.getOriginalFilename().equals("") && !foto.isEmpty()) {
				cli.setFoto(uniqueFilename);
			}
			cli.setNombres(usuario.getNombres());
			cli.setId_cliente(servCli.findByDni(loggedInUserName).getId_cliente());
			servCli.save(cli);
		}
		if(!password_final.equals("")) {
			usuario.setPassword(passwordEncoder.encode(password_final));
		}
		usuario.setId_usuario(id);
		serv.save(usuario);
		return "redirect:/usuario/perfil";
	}
	
	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("tituloPagina", "Usuario | Crear");
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", serv.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", serv.findByUsername(loggedInUserName).getRol().getRol());
		Usuario user = new Usuario();
		model.addAttribute("usuario",user);
		Collection<String> roles= Arrays.asList("CLIENTE");
		model.addAttribute("roles",servrol.findByEstadoAndRolNotIn(true,roles));
		return "usuario/create";
	}
	
	@PostMapping("/create")
	public String save(@Valid Usuario usuario,BindingResult result ,Model model,
			@RequestParam("fotoinput") MultipartFile foto,int rol_id) {
		model.addAttribute("tituloPagina", "Usuario | Crear");
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", serv.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", serv.findByUsername(loggedInUserName).getRol().getRol());
		String uniqueFilename="";
		uniqueFilename="perfil_anonimo.png";
		
		if(result.hasErrors()) {
			model.addAttribute("roles",servrol.findByEstado(true));
			return "/usuario/create";
		}
		if (!foto.isEmpty()) {		
			Path directorio =Paths.get(uploadDirectory);
			uniqueFilename = UUID.randomUUID().toString() + "_FOTO-USUARIO_" + foto.getOriginalFilename();
			String rutaAbsoluta=directorio.toFile().getAbsolutePath();
			try {
				byte[] bytesImg=foto.getBytes();
				Path rutaCompleta=Paths.get(rutaAbsoluta+"//"+uniqueFilename);
				Files.write(rutaCompleta, bytesImg);				
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}
		usuario.setFoto(uniqueFilename);
		usuario.setRol(servrol.getById(rol_id));
		usuario.setPassword(passwordEncoder.encode("password"));

		serv.save(usuario);
		
		
		return "redirect:/usuario/list";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable(value="id") int id,Model model) {
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", serv.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", serv.findByUsername(loggedInUserName).getRol().getRol());
		model.addAttribute("tituloPagina", "Usuario | Editar");
		model.addAttribute("usuario",serv.findById(id).get());		
		model.addAttribute("roles",servrol.findByEstado(true));
		model.addAttribute("id",id);
		return "usuario/edit";
	}
	
	@PostMapping("/update/{id}")
	public String update(@PathVariable(value="id") int id,Usuario usuario,@RequestParam("fotoinput") MultipartFile foto,int rol_id) {
		String uniqueFilename="";
		
		if(!foto.getOriginalFilename().equals("")) {
			if (!foto.isEmpty()) {		
				Path directorio =Paths.get(uploadDirectory);
				uniqueFilename = UUID.randomUUID().toString() + "_FOTO-USUARIO" + foto.getOriginalFilename();
				String rutaAbsoluta=directorio.toFile().getAbsolutePath();
				try {
					byte[] bytesImg=foto.getBytes();
					Path rutaCompleta=Paths.get(rutaAbsoluta+"//"+uniqueFilename);
					Files.write(rutaCompleta, bytesImg);				
				} catch (IOException e) {
					e.printStackTrace();
				}		
			}else {
				uniqueFilename="perfil_anonimo.png";			
			}
			usuario.setFoto(uniqueFilename);			
			servCli.findByDni(usuario.getUsername()).setFoto(uniqueFilename);
		}
		servCli.findByDni(usuario.getUsername()).setNombres(usuario.getNombres());
		servCli.save(servCli.findByDni(usuario.getUsername()));		
		usuario.setRol(servrol.getById(rol_id));	
		usuario.setId_usuario(id);
		serv.save(usuario);
		return "redirect:/usuario/list";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable(value="id") int id,Model model) {
		serv.findById(id).get().setEstado(false);
		serv.save(serv.findById(id).get());
		
		
		if(serv.findById(id).get().getRol().getRol().equals("CLIENTE")) {
			servCli.findByDni(serv.findById(id).get().getUsername()).setEstado(false);
			servCli.save(servCli.findByDni(serv.findById(id).get().getUsername()));
		}
		
		return "redirect:/usuario/list";

	}
	
	@GetMapping("/usuarioPDF")
	public void usuarioPDF(HttpServletResponse response) throws DocumentException, IOException{
		response.setContentType("application/pdf");
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Usuario_" + fechaActual + ".pdf";
		
		response.setHeader(cabecera, valor);
		
		List<Usuario> tabla = serv.findAll();
		
		UsuarioPDF exporter = new UsuarioPDF(tabla);
		exporter.exportar(response);
	}
	
	@GetMapping("/usuarioEXCEL")
	public void usuarioEXCEL(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/octet-stream");
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Usuario_" + fechaActual + ".xlsx";
		
		response.setHeader(cabecera, valor);
		
		List<Usuario> tabla = serv.findAll();
		
		UsuarioEXCEL exporter = new UsuarioEXCEL(tabla);
		exporter.exportar(response);
	}
}
