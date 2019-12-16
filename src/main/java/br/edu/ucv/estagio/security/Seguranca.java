package br.edu.ucv.estagio.security;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import br.edu.ucv.estagio.model.Pessoa;

@Named
@RequestScoped
public class Seguranca {

	@Inject
	private ExternalContext externalContext;
	
	private UsuarioSistema usuarioLogado = getUsuarioLogado();
	
	public Pessoa getAutenticado() {
		Pessoa autenticado = null;

		if (usuarioLogado != null) {
			autenticado = usuarioLogado.getAutenticado();
		}
		
		return autenticado;
	}

	@Produces
	@UsuarioLogado
	public UsuarioSistema getUsuarioLogado() {
		UsuarioSistema usuario = null;
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();

		if (auth != null && auth.getPrincipal() != null) {
			usuario = (UsuarioSistema) auth.getPrincipal();
		}
		
		return usuario;
	}
	
	public boolean isAdministradores() {
		return externalContext.isUserInRole("ADMINISTRADORES");
	}
	
	public boolean isAtendentes() {
		return externalContext.isUserInRole("ADMINISTRADORES") || externalContext.isUserInRole("ATENDENTES");
	}
	
	public boolean isAlunos() {
		return externalContext.isUserInRole("ADMINISTRADORES") || externalContext.isUserInRole("ALUNOS");
	}
}