package br.edu.ucv.estagio.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.edu.ucv.estagio.model.Grupo;
import br.edu.ucv.estagio.model.Pessoa;
import br.edu.ucv.estagio.repository.Usuarios;
import br.edu.ucv.estagio.util.cdi.CDIServiceLocator;

public class AppUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuarios usuarios = CDIServiceLocator.getBean(Usuarios.class);
		Pessoa usuario = usuarios.usuarioAtivo(username);
		
		UsuarioSistema autenticado = null;
		
		if (usuario != null) {
			autenticado = new UsuarioSistema(usuario, getGrupos(usuario));
		} else {
			throw new UsernameNotFoundException("Usuário não encontrado.");
		}
		
		return autenticado;
	}

	private Collection<? extends GrantedAuthority> getGrupos(Pessoa usuario) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		for (Grupo grupo : usuario.getUsuario().getGrupos()) {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + grupo.getNome().toUpperCase()));
		}
		
		return authorities;
	}
}