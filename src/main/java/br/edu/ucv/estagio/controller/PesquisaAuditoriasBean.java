package br.edu.ucv.estagio.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.edu.ucv.estagio.model.AcaoAuditoria;
import br.edu.ucv.estagio.model.Auditoria;
import br.edu.ucv.estagio.model.TabelaAuditoria;
import br.edu.ucv.estagio.repository.Auditorias;
import br.edu.ucv.estagio.repository.filter.AuditoriaFilter;

@Named
@ViewScoped
public class PesquisaAuditoriasBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Auditorias auditorias;
	
	private AuditoriaFilter filtro;
	private LazyDataModel<Auditoria> model;
	
	public PesquisaAuditoriasBean() {
		filtro = new AuditoriaFilter();
	}
	
	public void pesquisar() {
		model = new LazyDataModel<Auditoria>() {
			private static final long serialVersionUID = 1L;
				
			@Override
			public List<Auditoria> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setPropriedadeOrdenacao(sortField);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				
				setRowCount(auditorias.quantidadeFiltrados(filtro));
				return auditorias.filtrados(filtro);
			}
		};
	}

	public LazyDataModel<Auditoria> getModel() {
		return model;
	}

	public AuditoriaFilter getFiltro() {
		return filtro;
	}

	public void setFiltro(AuditoriaFilter filtro) {
		this.filtro = filtro;
	}
	
	public TabelaAuditoria[] getTabelas() {
		return TabelaAuditoria.values();
	}
	
	public AcaoAuditoria[] getAcoes() {
		return AcaoAuditoria.values();
	}
}