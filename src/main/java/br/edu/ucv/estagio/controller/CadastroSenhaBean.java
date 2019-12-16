package br.edu.ucv.estagio.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import br.edu.ucv.estagio.model.Pessoa;
import br.edu.ucv.estagio.model.TabelaAuditoria;
import br.edu.ucv.estagio.security.Seguranca;
import br.edu.ucv.estagio.service.CadastroAuditoriaService;
import br.edu.ucv.estagio.service.CadastroUsuarioService;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroSenhaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Seguranca seguranca;

	@Inject
	private CadastroAuditoriaService cadastroAuditoriaService;

	@Inject
	private CadastroUsuarioService cadastroPessoaService;

	private Pessoa pessoa;
	private String senhaAtual;
	private String senhaNova;

	public void inicializar() {
		if (seguranca.getAutenticado() != null) {
			setPessoa(seguranca.getAutenticado());
		}
	}

	public void salvar() {
		try {
			pessoa.getUsuario().conferirSenhas(senhaAtual, senhaNova);
			pessoa = cadastroPessoaService.salvar(pessoa);
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

	@NotBlank
	public String getSenhaAtual() {
		return senhaAtual;
	}

	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}

	@NotBlank
	@Size(min = 8, max = 20)
	public String getSenhaNova() {
		return senhaNova;
	}

	public void setSenhaNova(String senhaNova) {
		this.senhaNova = senhaNova;
	}
}