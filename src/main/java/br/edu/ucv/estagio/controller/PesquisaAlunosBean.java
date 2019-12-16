package br.edu.ucv.estagio.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.edu.ucv.estagio.model.Pessoa;
import br.edu.ucv.estagio.model.TabelaAuditoria;
import br.edu.ucv.estagio.repository.Alunos;
import br.edu.ucv.estagio.repository.filter.PessoaFilter;
import br.edu.ucv.estagio.service.CadastroAuditoriaService;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaAlunosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Alunos alunos;
	
	@Inject
	private CadastroAuditoriaService cadastroAuditoriaService;
	
	private PessoaFilter filtro;
	private LazyDataModel<Pessoa> model;
	
	private Pessoa pessoaSelecionada;
	
	public PesquisaAlunosBean() {
		filtro = new PessoaFilter();
	}
	
	public void excluir() {
		try {
			alunos.remover(pessoaSelecionada);
			cadastroAuditoriaService.salvar(TabelaAuditoria.PESSOA, pessoaSelecionada.getId(), true);
			FacesUtil.addInfoMessage("Aluno " + pessoaSelecionada.getNome() + " excluído com sucesso.");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public void pesquisar() {
		model = new LazyDataModel<Pessoa>() {
			private static final long serialVersionUID = 1L;
				
			@Override
			public List<Pessoa> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setPropriedadeOrdenacao(sortField);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				
				setRowCount(alunos.quantidadeFiltrados(filtro));
				return alunos.filtrados(filtro);
			}
		};
	}
	
	public LazyDataModel<Pessoa> getModel() {
		return model;
	}

	public PessoaFilter getFiltro() {
		return filtro;
	}

	public void setFiltro(PessoaFilter filtro) {
		this.filtro = filtro;
	}

	public Pessoa getPessoaSelecionada() {
		return pessoaSelecionada;
	}

	public void setPessoaSelecionada(Pessoa pessoaSelecionada) {
		this.pessoaSelecionada = pessoaSelecionada;
	}
}