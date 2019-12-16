package br.edu.ucv.estagio.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.edu.ucv.estagio.model.Pessoa;
import br.edu.ucv.estagio.model.TabelaAuditoria;
import br.edu.ucv.estagio.security.Seguranca;
import br.edu.ucv.estagio.service.CadastroAuditoriaService;
import br.edu.ucv.estagio.service.CadastroUsuarioService;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroPerfilBean extends CadastroComEnderecoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Seguranca seguranca;
	
	@Inject
	private CadastroAuditoriaService cadastroAuditoriaService;
	
	@Inject
	private CadastroUsuarioService cadastroUsuarioService;

	private Pessoa pessoa;

	public void inicializar() {
		if (seguranca.getAutenticado() != null) {
			setPessoa(seguranca.getAutenticado());
		}
		
		iniciarEnderecos(pessoa.getEndereco());
	}

	public void salvar() {
		try {
			pessoa = cadastroUsuarioService.salvar(pessoa);
			cadastroAuditoriaService.salvar(TabelaAuditoria.PESSOA, pessoa.getId(), false);
			FacesUtil.addInfoMessage("Usuário salvo com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
}