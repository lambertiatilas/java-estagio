package br.edu.ucv.estagio.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.edu.ucv.estagio.model.Instituicao;
import br.edu.ucv.estagio.model.TabelaAuditoria;
import br.edu.ucv.estagio.repository.Instituicoes;
import br.edu.ucv.estagio.repository.filter.InstituicaoFilter;
import br.edu.ucv.estagio.service.CadastroAuditoriaService;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaInstituicoesBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Instituicoes instituicoes;
	
	@Inject
	private CadastroAuditoriaService cadastroAuditoriaService;
	
	private InstituicaoFilter filtro;
	private LazyDataModel<Instituicao> model;
	
	private Instituicao instituicaoSelecionada;
	
	public PesquisaInstituicoesBean() {
		filtro = new InstituicaoFilter();
	}
	
	public void excluir() {
		try {
			instituicoes.remover(instituicaoSelecionada);
			cadastroAuditoriaService.salvar(TabelaAuditoria.INSTITUICAO, instituicaoSelecionada.getId(), true);
			FacesUtil.addInfoMessage("Instituição " + instituicaoSelecionada.getNome() + " excluída com sucesso.");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public void pesquisar() {
		model = new LazyDataModel<Instituicao>() {
			private static final long serialVersionUID = 1L;
				
			@Override
			public List<Instituicao> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setPropriedadeOrdenacao(sortField);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				
				setRowCount(instituicoes.quantidadeFiltrados(filtro));
				return instituicoes.filtradas(filtro);
			}
		};
	}
	
	public LazyDataModel<Instituicao> getModel() {
		return model;
	}

	public InstituicaoFilter getFiltro() {
		return filtro;
	}

	public Instituicao getInstituicaoSelecionada() {
		return instituicaoSelecionada;
	}

	public void setInstituicaoSelecionada(Instituicao instituicaoSelecionada) {
		this.instituicaoSelecionada = instituicaoSelecionada;
	}
}