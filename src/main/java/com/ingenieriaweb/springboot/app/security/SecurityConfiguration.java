package com.ingenieriaweb.springboot.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.ingenieriaweb.springboot.app.models.Rol;
import com.ingenieriaweb.springboot.app.models.Usuario;
import com.ingenieriaweb.springboot.app.models.service.IRolService;
import com.ingenieriaweb.springboot.app.models.service.IUsuarioService;
import com.ingenieriaweb.springboot.app.models.service.UserService;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserService userDetails;
	
	@Autowired
	private IUsuarioService user;
	
	@Autowired
	private IRolService servRol;
	
	@Lazy
	@Autowired
	private BCryptPasswordEncoder bycrypt;	
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		
		if(servRol.findByRol("ADMINISTRADOR")==null) {
			Rol rol=new Rol();
			rol.setRol("ADMINISTRADOR");
			servRol.save(rol);
			Rol rol1=new Rol();
			rol1.setRol("CLIENTE");
			servRol.save(rol1);
			Rol rol2=new Rol();
			rol2.setRol("VENDEDOR");
			servRol.save(rol2);
			Rol rol3=new Rol();
			rol3.setRol("GERENTE");
			servRol.save(rol3);
		}
		
		if(user.findByUsername("ADMIN")==null) {
			Usuario usu=new Usuario();
			usu.setUsername("ADMIN");
			usu.setFoto("perfil_anonimo.png");
			usu.setPassword(bycrypt.encode("password"));
			usu.setEmail("admin@unitru.edu.pe");
			usu.setNombres("ADMIN ADMIN");
			usu.setRol(null);
			user.save(usu);				
		}
		
		if(user.findByUsername("ADMIN")!=null) {
			if(user.findByUsername("ADMIN").getRol()==null) {
				Usuario userAdmin = user.findByUsername("ADMIN");
				Rol rolAdmin = servRol.findByRol("ADMINISTRADOR");
				userAdmin.setRol(rolAdmin);
				userAdmin.setId_usuario(userAdmin.getId_usuario());
				user.save(userAdmin);
			}
		}
		
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userDetails);
		auth.setPasswordEncoder(passwordEncoder());
		return auth;
	}
	
	
	@Override
	public  void  configure(AuthenticationManagerBuilder auth)  throws Exception{
		 /*auth.inMemoryAuthentication()
				.withUser("user")
				.password("password")
				.roles("USER");*/
		auth.authenticationProvider(authenticationProvider());
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers(
				"/home",
				"/",
				"/js/**",
				"/css/**",
				"/assets/**",
				"/foto_cliente/**",
				"/foto_video/**",
				"/img/**").permitAll()
		.anyRequest().authenticated()
		.and()
		.formLogin()
		.loginPage("/login")
		.permitAll()
		.and()
		.logout()
		.invalidateHttpSession(true)
		.clearAuthentication(true)
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessUrl("/login?logout")
		.permitAll();	
	}
}
