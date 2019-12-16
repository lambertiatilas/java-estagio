package br.edu.ucv.estagio.controller;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import br.edu.ucv.estagio.model.Pessoa;
import br.edu.ucv.estagio.service.EdicaoUsuarioService;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jsf.FacesUtil;

@Named
@RequestScoped
public class EdicaoPessoaBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Inject
	private EdicaoUsuarioService edicaoUsuarioService;
	
	@Inject
	@Edicao
	private Pessoa pessoa;
	
	@Inject
	private Event<EventPessoaAlterada> eventPessoaAlterada;
	
	public void resetarSenhaUsuario() {
		try {
			pessoa = edicaoUsuarioService.resetarSenhaUsuario(pessoa);
			eventPessoaAlterada.fire(new EventPessoaAlterada(pessoa));
			FacesUtil.addInfoMessage("Senha resetada com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public void alterarStatusUsuario() {
		try {
			pessoa = edicaoUsuarioService.alterarStatusUsuario(pessoa);
			eventPessoaAlterada.fire(new EventPessoaAlterada(pessoa));
			FacesUtil.addInfoMessage("O status do usuário foi alterado para " + pessoa.getUsuario().getStatus().getDescricao() + " !");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
}