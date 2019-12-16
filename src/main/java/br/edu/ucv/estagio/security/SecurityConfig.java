package br.edu.ucv.estagio.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
	public AppUserDetailsService userDetailsService() {
		return new AppUserDetailsService();
	}
	
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

	@Bean
	public PasswordEncoder passwordEncoder(){
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		JsfLoginUrlAuthenticationEntryPoint jsfLoginEntry = new JsfLoginUrlAuthenticationEntryPoint();
		jsfLoginEntry.setLoginFormUrl("/login.xhtml");
		jsfLoginEntry.setRedirectStrategy(new JsfRedirectStrategy());
		
		JsfAccessDeniedHandler jsfDeniedEntry = new JsfAccessDeniedHandler();
		jsfDeniedEntry.setLoginPath("/erro/403.xhtml");
		jsfDeniedEntry.setContextRelative(true);
		
		http
			.csrf().disable()
			.headers().frameOptions().sameOrigin()
			.and()
			
		.authorizeRequests()
			.antMatchers("/login.xhtml", "/erro/404.xhtml", "/javax.faces.resource/**").permitAll()
			.antMatchers("/home.xhtml", "/erro/403.xhtml", "/erro/500.xhtml").authenticated()
			.antMatchers("/estagio/**").hasAnyRole("ADMINISTRADORES", "ATENDENTES")
			.antMatchers("/bairro/**", "/empresa/**", "/estado/**", "/municipio/**", "/pais/**").hasRole("ADMINISTRADORES")
			.and()
		
		.formLogin()
			.loginPage("/login.xhtml")
			.failureUrl("/login.xhtml?invalid=true")
			.and()
		
		.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.and()
		
		.exceptionHandling()
			.accessDeniedPage("/erro/403.xhtml")
			.authenticationEntryPoint(jsfLoginEntry)
			.accessDeniedHandler(jsfDeniedEntry);
	}
}