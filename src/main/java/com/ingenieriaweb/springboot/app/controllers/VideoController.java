package com.ingenieriaweb.springboot.app.controllers;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import org.springframework.web.multipart.MultipartFile;

import com.ingenieriaweb.springboot.app.models.Formato;
import com.ingenieriaweb.springboot.app.models.Genero;
import com.ingenieriaweb.springboot.app.models.Idioma;
import com.ingenieriaweb.springboot.app.models.Rol;
import com.ingenieriaweb.springboot.app.models.Usuario;
import com.ingenieriaweb.springboot.app.models.Video;
import com.ingenieriaweb.springboot.app.models.GeneroVideo;
import com.ingenieriaweb.springboot.app.models.VideoGenero;
import com.ingenieriaweb.springboot.app.models.VideoIdioma;
import com.ingenieriaweb.springboot.app.models.service.IFormatoService;
import com.ingenieriaweb.springboot.app.models.service.IGeneroService;
import com.ingenieriaweb.springboot.app.models.service.IIdiomaService;
import com.ingenieriaweb.springboot.app.models.service.IUsuarioService;
import com.ingenieriaweb.springboot.app.models.service.IVideoGeneroService;
import com.ingenieriaweb.springboot.app.models.service.IVideoIdiomaService;
import com.ingenieriaweb.springboot.app.models.service.IVideoService;
import com.ingenieriaweb.springboot.app.paginator.PageRender;
import com.ingenieriaweb.springboot.app.reports.UsuarioEXCEL;
import com.ingenieriaweb.springboot.app.reports.UsuarioPDF;
import com.ingenieriaweb.springboot.app.reports.VideoEXCEL;
import com.ingenieriaweb.springboot.app.reports.VideoPDF;
import com.lowagie.text.DocumentException;

@Controller
@RequestMapping("/video")
@SessionAttributes("video")
public class VideoController {
	
	@Autowired
	private IVideoService serv;
	
	@Autowired
	private IFormatoService servform;
	
	@Autowired
	private IIdiomaService servidi;
	
	@Autowired
	private IGeneroService servgen;
	
	@Autowired
	private IVideoGeneroService servvidG;
	
	@Autowired
	private IVideoIdiomaService servvidI;
	
	@Autowired
	private IUsuarioService servuser;

	public static String uploadDirectoryVideo= System.getProperty("user.dir")+"/src/main/webapp/foto_video";
	
	@GetMapping("/list")
	public String Listar(@RequestParam(name = "page", defaultValue = "0") int page
			,@RequestParam(name = "titulo", defaultValue = "") String titulo
			,@RequestParam(name = "anio", defaultValue = "") String anio
			,@RequestParam(name = "buscarFormato", defaultValue = "") String buscarFormato
			,@RequestParam(name = "buscarGenero", defaultValue = "") String buscarGenero
			,@RequestParam(name = "buscarIdioma", defaultValue = "") String buscarIdioma,Pageable pageable,
			Model model) {
		
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());
		model.addAttribute("tituloPagina", "Video | Listar");
		
		Pageable pageRequest = PageRequest.of(page,5);
		
		Page<Video> videos =serv.findByEstadoAndTituloContainingAndAnioStartingWithAndFormatoIn(true,titulo,anio,
				servform.findByEstadoAndFormatoStartingWith(true, buscarFormato),pageRequest);
		
		PageRender<Video> pageRender = new PageRender<Video>("/video/list", videos);
		
		model.addAttribute("page", pageRender);
		model.addAttribute("titulo", titulo);	
		model.addAttribute("anio", anio);	
		model.addAttribute("buscarFormato", buscarFormato);		
		model.addAttribute("buscarIdioma", buscarIdioma);
		model.addAttribute("buscarGenero", buscarGenero);		
		model.addAttribute("video",videos.getContent());
		return "video/list";
	}
	
	@GetMapping("/listvideos")
	public String ListarVideos(@RequestParam(name = "page", defaultValue = "0") int page
			,@RequestParam(name = "titulo", defaultValue = "") String titulo
			,@RequestParam(name = "anio", defaultValue = "") String anio
			,@RequestParam(name = "buscarFormato", defaultValue = "") String buscarFormato
			,@RequestParam(name = "buscarGenero", defaultValue = "") String buscarGenero
			,@RequestParam(name = "buscarIdioma", defaultValue = "") String buscarIdioma,Pageable pageable,
			Model model) {
		
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());
		model.addAttribute("tituloPagina", "Video | Listar");
		
		Pageable pageRequest = PageRequest.of(page,10);
		
		Page<Video> videos =serv.findByEstadoAndTituloContainingAndAnioStartingWithAndFormatoIn(false,titulo,anio,
				servform.findByEstadoAndFormatoStartingWith(true, buscarFormato),pageRequest);	
		
		PageRender<Video> pageRender = new PageRender<Video>("/video/listvideos", videos);
		
		model.addAttribute("page", pageRender);
		model.addAttribute("titulo", titulo);	
		model.addAttribute("anio", anio);	
		model.addAttribute("buscarFormato", buscarFormato);		
		model.addAttribute("buscarIdioma", buscarIdioma);
		model.addAttribute("buscarGenero", buscarGenero);		
		model.addAttribute("video",videos.getContent());
		return "video/listvideos";
	}
	
	
	 
	
	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("tituloPagina", "Video | Crear");
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());
		Video video = new Video();
		model.addAttribute("video",video);
		model.addAttribute("idiomas",servidi.findByEstado(true));
		model.addAttribute("formatos",servform.findByEstado(true));
		model.addAttribute("generos",servgen.findByEstado(true));
		return "video/create";
	}
	
	@PostMapping("/create")
	public String save(@Valid Video video,BindingResult result ,Model model,
			@RequestParam("fotoinput") MultipartFile foto,int id_form,String id_gen,String id_idi) {
		model.addAttribute("tituloPagina", "Video | Crear");
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());

		String uniqueFilename="";		
		
		if(result.hasErrors()) {
			model.addAttribute("idiomas",servidi.findByEstado(true));
			model.addAttribute("formatos",servform.findByEstado(true));
			model.addAttribute("generos",servgen.findByEstado(true));
			return "/video/create";
		}
		
		if (!foto.isEmpty()) {		
			Path directorio =Paths.get(uploadDirectoryVideo);
			uniqueFilename = UUID.randomUUID().toString() + "_FOTO-VIDEO_" + foto.getOriginalFilename();
			String rutaAbsoluta=directorio.toFile().getAbsolutePath();
			try {
				byte[] bytesImg=foto.getBytes();
				Path rutaCompleta=Paths.get(rutaAbsoluta+"//"+uniqueFilename);
				System.out.print(rutaCompleta);
				Files.write(rutaCompleta, bytesImg);				
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}		
		
		video.setPortada(uniqueFilename);
		
		video.setFormato(servform.getById(id_form));		
		
		 String [] vectGen = id_gen.split(",");		 
		 for(int i=0; i<vectGen.length;i++) {
			 Genero gen= servgen.getById(Integer.parseInt(vectGen[i]));
			 VideoGenero itemGenero = new VideoGenero();
			 itemGenero.setGenero(gen);
			 video.addVideoGenero(itemGenero);
		 }
		 
		 
		 String [] vectIdi = id_idi.split(",");		 
		 for(int i=0; i<vectIdi.length;i++) {
			 Idioma idi= servidi.getById(Integer.parseInt(vectIdi[i]));
			 VideoIdioma itemIdioma = new VideoIdioma();
			 itemIdioma.setIdioma(idi);
			 video.addVideoIdioma(itemIdioma);
		 }
		
		serv.save(video);
		
		
		return "redirect:/video/list";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable(value="id") int id,Model model) {
		model.addAttribute("tituloPagina", "Video | Editar");
		model.addAttribute("video",serv.findById(id).get());		
		model.addAttribute("idiomas",servidi.findByEstado(true));
		model.addAttribute("formatos",servform.findByEstado(true));
		model.addAttribute("generos",servgen.findByEstado(true));
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("usuario_autenticado", servuser.findByUsername(loggedInUserName));
		model.addAttribute("rol_autenticado", servuser.findByUsername(loggedInUserName).getRol().getRol());
		
		List<VideoGenero> itemGenero =serv.getById(id).getList_video_genero();
		List<Integer> generos = new ArrayList<Integer>();
		for(int i=0; i<itemGenero.size();i++) {						
			generos.add(itemGenero.get(i).getGenero().getId_genero());
		}
		
		
		List<VideoIdioma> itemIdioma =serv.getById(id).getList_video_idioma();
		List<Integer> idiomas = new ArrayList<Integer>();
		for(int i=0; i<itemIdioma.size();i++) {						
			idiomas.add(itemIdioma.get(i).getIdioma().getId_idioma());
		}	
		
		model.addAttribute("idGeneros",generos);
		model.addAttribute("idIdiomas",idiomas);
		model.addAttribute("id",id);
		return "video/edit";
	}
	
	@PostMapping("/update/{id}")
	public String update(@PathVariable(value="id") int id,@Valid Video video,
			@RequestParam("fotoinput") MultipartFile foto,int id_form,String id_gen,String id_idi) {

		String uniqueFilename="";	
				
		if(!foto.getOriginalFilename().equals("")) {
			if (!foto.isEmpty()) {		
				Path directorio =Paths.get(uploadDirectoryVideo);
				uniqueFilename = UUID.randomUUID().toString() + "_FOTO-VIDEO_" + foto.getOriginalFilename();
				String rutaAbsoluta=directorio.toFile().getAbsolutePath();
				try {
					byte[] bytesImg=foto.getBytes();
					Path rutaCompleta=Paths.get(rutaAbsoluta+"//"+uniqueFilename);
					Files.write(rutaCompleta, bytesImg);				
				} catch (IOException e) {
					e.printStackTrace();
				}		
			}
			video.setPortada(uniqueFilename);
		}		
		
		video.setFormato(servform.getById(id_form));
		
		servvidG.EliminarDetalle(id);
		servvidI.EliminarDetalle(id);		
		
		List<VideoGenero> itemVideosGeneros = new ArrayList<VideoGenero>();	
		 String [] vectGen = id_gen.split(",");		 
		 for(int i=0; i<vectGen.length;i++) {
			 Genero gen= servgen.getById(Integer.parseInt(vectGen[i]));
			 VideoGenero itemGenero = new VideoGenero();
			 itemGenero.setGenero(gen);
			 itemVideosGeneros.add(itemGenero);
		 }
		 
		List<VideoIdioma> itemVideosIdiomas = new ArrayList<VideoIdioma>();	

		 String [] vectIdi = id_idi.split(",");		 
		 for(int i=0; i<vectIdi.length;i++) {
			 Idioma idi= servidi.getById(Integer.parseInt(vectIdi[i]));
			 VideoIdioma itemIdioma = new VideoIdioma();
			 itemIdioma.setIdioma(idi);
			 itemVideosIdiomas.add(itemIdioma);
		 }
		 video.setList_video_genero(itemVideosGeneros);
		 video.setList_video_idioma(itemVideosIdiomas);
		 video.setId_video(id);		
		serv.save(video);
		return "redirect:/video/list";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable(value="id") int id,Model model) {
		serv.findById(id).get().setEstado(false);
		serv.save(serv.findById(id).get());
		return "redirect:/video/list";

	}
	
	@GetMapping("/resturar/{id}")
	public String resturar(@PathVariable(value="id") int id,Model model) {
		serv.findById(id).get().setEstado(true);
		serv.save(serv.findById(id).get());
		return "redirect:/video/listvideos";

	}
	
	@GetMapping("/videoPDF")
	public void videoPDF(HttpServletResponse response) throws DocumentException, IOException{
		response.setContentType("application/pdf");
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Video_" + fechaActual + ".pdf";
		
		response.setHeader(cabecera, valor);
		
		List<Video> tabla = serv.findAll();
		
		VideoPDF exporter = new VideoPDF(tabla);
		exporter.exportar(response);
	}
	
	@GetMapping("/videoEXCEL")
	public void videoEXCEL(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/octet-stream");
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Video_" + fechaActual + ".xlsx";
		
		response.setHeader(cabecera, valor);
		
		List<Video> tabla = serv.findAll();
		
		VideoEXCEL exporter = new VideoEXCEL(tabla);
		exporter.exportar(response);
	}

}
