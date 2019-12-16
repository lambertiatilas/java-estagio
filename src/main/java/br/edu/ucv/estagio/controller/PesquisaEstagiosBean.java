package br.edu.ucv.estagio.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.edu.ucv.estagio.model.Estagio;
import br.edu.ucv.estagio.model.TabelaAuditoria;
import br.edu.ucv.estagio.repository.Estagios;
import br.edu.ucv.estagio.repository.filter.EstagioFilter;
import br.edu.ucv.estagio.service.CadastroAuditoriaService;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaEstagiosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Estagios estagios;
	
	@Inject
	private CadastroAuditoriaService cadastroAuditoriaService;

	private EstagioFilter filtro;
	private LazyDataModel<Estagio> model;

	private Estagio estagioSelecionado;

	public PesquisaEstagiosBean() {
		filtro = new EstagioFilter();
	}

	public void excluir() {
		try {
			estagios.remover(estagioSelecionado);
			cadastroAuditoriaService.salvar(TabelaAuditoria.ESTAGIO, estagioSelecionado.getId(), true);
			FacesUtil.addInfoMessage("Estágio " + estagioSelecionado.getId() + " excluído com sucesso.");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}

	public void pesquisar() {
		model = new LazyDataModel<Estagio>() {
			private static final long serialVersionUID = 1L;
				
			@Override
			public List<Estagio> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setPropriedadeOrdenacao(sortField);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				
				setRowCount(estagios.quantidadeFiltrados(filtro));
				return estagios.filtrados(filtro);
			}
		};
	}
	
	public LazyDataModel<Estagio> getModel() {
		return model;
	}

	public EstagioFilter getFiltro() {
		return filtro;
	}

	public Estagio getEstagioSelecionado() {
		return estagioSelecionado;
	}

	public void setEstagioSelecionado(Estagio estagioSelecionado) {
		this.estagioSelecionado = estagioSelecionado;
	}
}