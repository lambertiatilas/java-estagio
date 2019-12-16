package br.edu.ucv.estagio.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.edu.ucv.estagio.model.Clausula;
import br.edu.ucv.estagio.model.TabelaAuditoria;
import br.edu.ucv.estagio.model.TipoClausula;
import br.edu.ucv.estagio.repository.Clausulas;
import br.edu.ucv.estagio.repository.filter.ClausulaFilter;
import br.edu.ucv.estagio.service.CadastroAuditoriaService;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaClausulasBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Clausulas clausulas;
	
	@Inject
	private CadastroAuditoriaService cadastroAuditoriaService;

	private ClausulaFilter filtro;
	private LazyDataModel<Clausula> model;

	private Clausula clausulaSelecionada;

	public PesquisaClausulasBean() {
		filtro = new ClausulaFilter();
	}

	public void excluir() {
		try {
			clausulas.remover(clausulaSelecionada);
			cadastroAuditoriaService.salvar(TabelaAuditoria.CLAUSULA, clausulaSelecionada.getId(), true);
			FacesUtil.addInfoMessage("Cláusula " + clausulaSelecionada.getId() + " excluída com sucesso.");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}

	public void pesquisar() {
		model = new LazyDataModel<Clausula>() {
			private static final long serialVersionUID = 1L;
				
			@Override
			public List<Clausula> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setPropriedadeOrdenacao(sortField);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				
				setRowCount(clausulas.quantidadeFiltrados(filtro));
				return clausulas.filtradas(filtro);
			}
		};
	}
	
	public LazyDataModel<Clausula> getModel() {
		return model;
	}
	
	public ClausulaFilter getFiltro() {
		return filtro;
	}

	public Clausula getClausulaSelecionada() {
		return clausulaSelecionada;
	}

	public void setClausulaSelecionada(Clausula clausulaSelecionada) {
		this.clausulaSelecionada = clausulaSelecionada;
	}
	
	public TipoClausula[] getTipos() {
		return TipoClausula.values();
	}
}