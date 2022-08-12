package com.ingenieriaweb.springboot.app.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.ingenieriaweb.springboot.app.models.Cliente;
import com.ingenieriaweb.springboot.app.models.Formato;
import com.ingenieriaweb.springboot.app.models.Rol;
import com.ingenieriaweb.springboot.app.models.Urbanizacion;
import com.ingenieriaweb.springboot.app.models.Usuario;
import com.ingenieriaweb.springboot.app.models.service.IClienteService;
import com.ingenieriaweb.springboot.app.models.service.IRolService;
import com.ingenieriaweb.springboot.app.models.service.IUrbanizacionService;
import com.ingenieriaweb.springboot.app.models.service.IUsuarioService;
import com.ingenieriaweb.springboot.app.paginator.PageRender;
import com.ingenieriaweb.springboot.app.reports.ClienteEXCEL;
import com.ingenieriaweb.springboot.app.reports.ClientePDF;
import com.ingenieriaweb.springboot.app.reports.FormatoEXCEL;
import com.ingenieriaweb.springboot.app.reports.FormatoPDF;
import com.lowagie.text.DocumentException;

@Controller
@RequestMapping("/cliente")
@SessionAttributes("cliente")
public class ClienteController {
	
	@Autowired
	private IClienteService serv;
	
	@Autowired
	private IUrbanizacionService servurb;
	
	@Autowired
	private IUsuarioService servuser;
	
	@Autowired
	private IRolService servrol;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	String validado="0",dnilist="";
	
	@GetMapping(value = "/clientes/{term}", produces = { "application/json" })
	public @ResponseBody List<ClienteClass> cargarCliente(@PathVariable String term) {
		List<ClienteClass> ListClie = new ArrayList<ClienteClass>();
		for(Cliente cli :  serv.findDistinctNombresByEstadoAndNombresStartingWith(true,term)) {
			ClienteClass clientes=new ClienteClass();
			clientes.setDni(cli.getDni());
			clientes.setId(cli.getId_cliente());
			clientes.setNombres(cli.getNombres());
			clientes.setDninombres(cli.getNombres()+" - "+cli.getDni());
			clientes.setDireccion(cli.getDireccion());
			clientes.setFoto(cli.getFoto());
			clientes.setEdad(cli.getEdad());
			clientes.setGenero(cli.getGenero());
			clientes.setTelefono(cli.getTelefono());
			clientes.setUrbanizacion(cli.getUrbanizacion().getUrbanizacion());
			ListClie.add(clientes);
		}		
		return ListClie;
	}
	
	
	@GetMapping("/list")
	public String Listar(@RequestParam(name = "page", defaultValue = "0") int page
			,@RequestParam(name = "dni", defaultValue = "") String dni
			,@RequestParam(name = "nombres", defaultValue = "") String nombres
			,@RequestParam(name = "buscarUrbanizacion", defaultValue = "") String buscarUrbanizacion
			,@RequestParam(name = "sexoinput", defaultValue = "") String sexoinput
			,Model model) {
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());
		model.addAttribute("tituloPagina", "Cliente | Listar");
		

		Pageable pageRequest = PageRequest.of(page,10);		
		Page<Cliente> clientes =serv.findByEstadoAndDniStartingWithAndNombresStartingWithAndGeneroStartingWithAndUrbanizacionIn
				(true,dni,nombres,sexoinput,
				servurb.findByEstadoAndUrbanizacionStartingWith(true, buscarUrbanizacion),pageRequest);
		PageRender<Cliente> pageRender = new PageRender<Cliente>("/cliente/list", clientes);
		model.addAttribute("page", pageRender);
		model.addAttribute("dni", dni);	
		model.addAttribute("nombres", nombres);	
		model.addAttribute("sexoinput", sexoinput);
		model.addAttribute("buscarUrbanizacion", buscarUrbanizacion);		
		model.addAttribute("validado",validado);
		model.addAttribute("dnilist",dnilist);		
		model.addAttribute("cliente",clientes.getContent());
		
		return "cliente/list";
	}
	
	@GetMapping("/create")
	public String create(Model model) {
		validado="1";
		dnilist="";
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());
		model.addAttribute("tituloPagina", "Cliente | Crear");
		Cliente cliente = new Cliente();
		model.addAttribute("cliente",cliente);
		model.addAttribute("urbanizaciones",servurb.findByEstado(true));
		model.addAttribute("urb","");

		return "cliente/create";
	}
	
	@PostMapping("/create")
	public String save(@Valid Cliente cliente,BindingResult result ,Model model,
			@RequestParam("fotoinput") MultipartFile foto,String urb) {
		System.out.print(foto);
		model.addAttribute("tituloPagina", "Cliente | Crear");
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());
		String uniqueFilename="";
		if(cliente.getGenero().equals("Hombre")) {
			uniqueFilename="perfil_anonimoH.png";
		}else {
			uniqueFilename="perfil_anonimoM.png";
		}
		
		if(result.hasErrors()) {
			model.addAttribute("urbanizaciones",servurb.findByEstado(true));
			model.addAttribute("validado","0");
			return "/cliente/create";
		}
		if (!foto.isEmpty()) {		
			Path directorio =Paths.get(UsuarioController.uploadDirectory);
			uniqueFilename = UUID.randomUUID().toString() + "_FOTO-CLIENTE" + foto.getOriginalFilename();
			String rutaAbsoluta=directorio.toFile().getAbsolutePath();
			try {
				byte[] bytesImg=foto.getBytes();
				Path rutaCompleta=Paths.get(rutaAbsoluta+"//"+uniqueFilename);
				Files.write(rutaCompleta, bytesImg);				
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}
		
		//			cliente.setUrbanizacion(servurb.getById(id_urb));		

		if(servurb.findByUrbanizacion(urb)==null) {
			Urbanizacion urba= new Urbanizacion();
			urba.setUrbanizacion(urb);
			servurb.save(urba);
		}
		
		cliente.setUrbanizacion(servurb.findByUrbanizacion(urb));
		cliente.setFoto(uniqueFilename);
		serv.save(cliente);
		
		Usuario user=new Usuario();
		user.setFoto(uniqueFilename);
		user.setUsername(cliente.getDni());
		user.setNombres(cliente.getNombres());
		user.setRol(servrol.findByRol("CLIENTE"));
		user.setPassword(passwordEncoder.encode("password"));

		servuser.save(user);
		validado="1";
		dnilist=cliente.getDni();		
		return "redirect:/cliente/list";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable(value="id") int id,Model model) {
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());
		model.addAttribute("tituloPagina", "Cliente | Editar");
		model.addAttribute("cliente",serv.findById(id).get());		
		model.addAttribute("urbanizaciones",servurb.findByEstado(true));
		model.addAttribute("urb",serv.findById(id).get().getUrbanizacion().getUrbanizacion());

		model.addAttribute("id",id);
		return "cliente/edit";
	}
	
	
	@GetMapping("/datoGenerales")
	public String datosGenerales(Model model) {
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());
		model.addAttribute("tituloPagina", "Usuario | Datos Generales");
		model.addAttribute("cliente",serv.findById(serv.findByDni(loggedInUserName).getId_cliente()).get());		
		model.addAttribute("urbanizaciones",servurb.findByEstado(true));
		model.addAttribute("id",serv.findByDni(loggedInUserName).getId_cliente());
		return "usuario/datoGenerales";
	}
	
	@PostMapping("/datosUpdate/{id}")
	public String datosUpdate(@PathVariable(value="id") int id,Cliente cliente,int id_urb) {		
		cliente.setUrbanizacion(servurb.getById(id_urb));	
		cliente.setId_cliente(id);
		serv.save(cliente);
		return "redirect:/cliente/datoGenerales";
	}

	
	@PostMapping("/update/{id}")
	public String update(@PathVariable(value="id") int id,Cliente cliente,@RequestParam("fotoinput") MultipartFile foto,String urb) {
		String uniqueFilename="";
		
		if(!foto.getOriginalFilename().equals("")) {
			if (!foto.isEmpty()) {		
				Path directorio =Paths.get(UsuarioController.uploadDirectory);
				uniqueFilename = UUID.randomUUID().toString() + "_FOTO-CLIENTE" + foto.getOriginalFilename();
				String rutaAbsoluta=directorio.toFile().getAbsolutePath();
				try {
					byte[] bytesImg=foto.getBytes();
					Path rutaCompleta=Paths.get(rutaAbsoluta+"//"+uniqueFilename);
					Files.write(rutaCompleta, bytesImg);				
				} catch (IOException e) {
					e.printStackTrace();
				}		
			}else {
				if(cliente.getGenero().equals("Hombre")) {
					uniqueFilename="perfil_anonimoH.png";
				}else {
					uniqueFilename="perfil_anonimoM.png";
				}			
			}
			
			cliente.setFoto(uniqueFilename);
			servuser.findByUsername(serv.getById(id).getDni()).setFoto(uniqueFilename);

		};
		servuser.findByUsername(serv.getById(id).getDni()).setNombres(cliente.getNombres());
		servuser.save(servuser.findByUsername(serv.getById(id).getDni()));		
		if(servurb.findByUrbanizacion(urb)==null) {
			Urbanizacion urba= new Urbanizacion();
			urba.setUrbanizacion(urb);
			servurb.save(urba);
		}
		
		cliente.setUrbanizacion(servurb.findByUrbanizacion(urb));	
		cliente.setId_cliente(id);
		serv.save(cliente);
		return "redirect:/cliente/list";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable(value="id") int id,Model model) {
		
		serv.findById(id).get().setEstado(false);
		serv.save(serv.findById(id).get());
		
		servuser.findByUsername(serv.getById(id).getDni()).setEstado(false);
		servuser.save(servuser.findByUsername(serv.getById(id).getDni()));
		return "redirect:/cliente/list";

	}
	
	@GetMapping("/clientePDF")
	public void clientePDF(HttpServletResponse response) throws DocumentException, IOException{
		response.setContentType("application/pdf");
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Cliente_" + fechaActual + ".pdf";
		
		response.setHeader(cabecera, valor);
		
		List<Cliente> tabla = serv.findAll();
		
		ClientePDF exporter = new ClientePDF(tabla);
		exporter.exportar(response);
	}
	
	@GetMapping("/clienteEXCEL")
	public void clienteEXCEL(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/octet-stream");
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Cliente_" + fechaActual + ".xlsx";
		
		response.setHeader(cabecera, valor);
		
		List<Cliente> tabla = serv.findAll();
		
		ClienteEXCEL exporter = new ClienteEXCEL(tabla);
		exporter.exportar(response);
	}
}
