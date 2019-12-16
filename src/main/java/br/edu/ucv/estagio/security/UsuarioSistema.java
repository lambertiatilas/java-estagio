package br.edu.ucv.estagio.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import br.edu.ucv.estagio.model.Pessoa;

public class UsuarioSistema extends User {

	private static final long serialVersionUID = 1L;
	
	private Pessoa autenticado;
	
	public UsuarioSistema(Pessoa autenticado, Collection<? extends GrantedAuthority> authorities) {
		super(autenticado.getCpf(), autenticado.getUsuario().getSenha(), authorities);
		this.autenticado = autenticado;
	}

	public Pessoa getAutenticado() {
		return autenticado;
	}

	public void setAutenticado(Pessoa autenticado) {
		this.autenticado = autenticado;
	}
}