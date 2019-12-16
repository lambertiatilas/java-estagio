package br.edu.ucv.estagio.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.edu.ucv.estagio.model.Estado;
import br.edu.ucv.estagio.model.TabelaAuditoria;
import br.edu.ucv.estagio.repository.Estados;
import br.edu.ucv.estagio.repository.filter.EstadoFilter;
import br.edu.ucv.estagio.service.CadastroAuditoriaService;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaEstadosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Estados estados;
	
	@Inject
	private CadastroAuditoriaService cadastroAuditoriaService;
	
	private EstadoFilter filtro;
	private LazyDataModel<Estado> model;
	
	private Estado estadoSelecionado;
	
	public PesquisaEstadosBean() {
		filtro = new EstadoFilter();
	}
	
	public void excluir() {
		try {
			estados.remover(estadoSelecionado);
			cadastroAuditoriaService.salvar(TabelaAuditoria.ESTADO, estadoSelecionado.getId(), true);
			FacesUtil.addInfoMessage("Estado " + estadoSelecionado.getNome() + " excluído com sucesso.");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public void pesquisar() {
		model = new LazyDataModel<Estado>() {
			private static final long serialVersionUID = 1L;
				
			@Override
			public List<Estado> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setPropriedadeOrdenacao(sortField);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				
				setRowCount(estados.quantidadeFiltrados(filtro));
				return estados.filtrados(filtro);
			}
		};
	}
	
	public LazyDataModel<Estado> getModel() {
		return model;
	}

	public EstadoFilter getFiltro() {
		return filtro;
	}

	public Estado getEstadoSelecionado() {
		return estadoSelecionado;
	}

	public void setEstadoSelecionado(Estado estadoSelecionado) {
		this.estadoSelecionado = estadoSelecionado;
	}
}