package br.edu.ucv.estagio.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.edu.ucv.estagio.model.Pais;
import br.edu.ucv.estagio.model.TabelaAuditoria;
import br.edu.ucv.estagio.repository.Paises;
import br.edu.ucv.estagio.repository.filter.PaisFilter;
import br.edu.ucv.estagio.service.CadastroAuditoriaService;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaPaisesBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Paises paises;
	
	@Inject
	private CadastroAuditoriaService cadastroAuditoriaService;
	
	private PaisFilter filtro;
	private LazyDataModel<Pais> model;
	
	private Pais paisSelecionado;
	
	public PesquisaPaisesBean() {
		filtro = new PaisFilter();
	}
	
	public void excluir() {
		try {
			paises.remover(paisSelecionado);
			cadastroAuditoriaService.salvar(TabelaAuditoria.PAIS, paisSelecionado.getId(), true);
			FacesUtil.addInfoMessage("Pais " + paisSelecionado.getNome() + " excluído com sucesso.");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public void pesquisar() {
		model = new LazyDataModel<Pais>() {
			private static final long serialVersionUID = 1L;
				
			@Override
			public List<Pais> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setPropriedadeOrdenacao(sortField);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				
				setRowCount(paises.quantidadeFiltrados(filtro));
				return paises.filtrados(filtro);
			}
		};
	}
	
	public LazyDataModel<Pais> getModel() {
		return model;
	}

	public PaisFilter getFiltro() {
		return filtro;
	}

	public Pais getPaisSelecionado() {
		return paisSelecionado;
	}

	public void setPaisSelecionado(Pais paisSelecionado) {
		this.paisSelecionado = paisSelecionado;
	}
}