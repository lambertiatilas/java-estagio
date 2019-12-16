package br.edu.ucv.estagio.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.edu.ucv.estagio.model.Contato;
import br.edu.ucv.estagio.model.Grupo;
import br.edu.ucv.estagio.model.Pessoa;
import br.edu.ucv.estagio.model.TabelaAuditoria;
import br.edu.ucv.estagio.model.TipoPessoa;
import br.edu.ucv.estagio.model.Usuario;
import br.edu.ucv.estagio.repository.Grupos;
import br.edu.ucv.estagio.service.CadastroAuditoriaService;
import br.edu.ucv.estagio.service.CadastroUsuarioService;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroUsuarioBean extends CadastroComEnderecoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Grupos grupos;
	
	@Inject
	private CadastroAuditoriaService cadastroAuditoriaService;
	
	@Inject
	private CadastroUsuarioService cadastroUsuarioService;
	
	@Produces
	@Edicao
	private Pessoa pessoa;
	
	private List<Grupo> listaGrupos = new ArrayList<>();

	public void inicializar() {
		if (pessoa == null) {
			limpar();
		}
		
		iniciarEnderecos(pessoa.getEndereco());
		listaGrupos = grupos.grupos();
	}

	private void limpar() {
		pessoa = new Pessoa();
		pessoa.setEndereco(novoEndereco());
		pessoa.setContato(new Contato());
		pessoa.setUsuario(new Usuario());
	}
	
	public void pessoaAlterada(@Observes EventPessoaAlterada event) {
		pessoa = event.getPessoa();
	}

	public void salvar() {
		try {
			pessoa = cadastroUsuarioService.salvar(pessoa);
			cadastroAuditoriaService.salvar(TabelaAuditoria.PESSOA, pessoa.getId(), false);
			limpar();
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
	
	public List<Grupo> getListaGrupos() {
		return listaGrupos;
	}
	
	public TipoPessoa[] getTipos() {
		return TipoPessoa.values();
	}

	public boolean isEditando() {
		return pessoa.isExistente();
	}
}