package br.edu.ucv.estagio.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.edu.ucv.estagio.model.Contato;
import br.edu.ucv.estagio.model.Pessoa;
import br.edu.ucv.estagio.model.TabelaAuditoria;
import br.edu.ucv.estagio.model.Usuario;
import br.edu.ucv.estagio.service.CadastroAlunoService;
import br.edu.ucv.estagio.service.CadastroAuditoriaService;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroAlunoBean extends CadastroComEnderecoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private CadastroAuditoriaService cadastroAuditoriaService;
	
	@Inject
	private CadastroAlunoService cadastroAlunoService;
	
	private Pessoa pessoa;
	
	public void inicializar() {
		if (pessoa == null) {
			limpar();
		}
		
		iniciarEnderecos(pessoa.getEndereco());
	}

	private void limpar() {
		pessoa = new Pessoa();
		pessoa.setEndereco(novoEndereco());
		pessoa.setContato(new Contato());
		pessoa.setUsuario(new Usuario());
	}
	
	public void salvar() {
		try {
			pessoa = cadastroAlunoService.salvar(pessoa);
			cadastroAuditoriaService.salvar(TabelaAuditoria.PESSOA, pessoa.getId(), false);
			limpar();
			FacesUtil.addInfoMessage("Aluno salvo com sucesso!");
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

	public boolean isEditando() {
		return pessoa.isExistente();
	}
}