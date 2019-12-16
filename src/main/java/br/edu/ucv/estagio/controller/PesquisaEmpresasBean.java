package br.edu.ucv.estagio.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.edu.ucv.estagio.model.Empresa;
import br.edu.ucv.estagio.model.TabelaAuditoria;
import br.edu.ucv.estagio.repository.Empresas;
import br.edu.ucv.estagio.repository.filter.EmpresaFilter;
import br.edu.ucv.estagio.service.CadastroAuditoriaService;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaEmpresasBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Empresas empresas;
	
	@Inject
	private CadastroAuditoriaService cadastroAuditoriaService;
	
	private EmpresaFilter filtro;
	private LazyDataModel<Empresa> model;
	
	private Empresa empresaSelecionada;
	
	public PesquisaEmpresasBean() {
		filtro = new EmpresaFilter();
	}
	
	public void excluir() {
		try {
			empresas.remover(empresaSelecionada);
			cadastroAuditoriaService.salvar(TabelaAuditoria.EMPRESA, empresaSelecionada.getId(), true);
			FacesUtil.addInfoMessage("Empresa " + empresaSelecionada.getRazaoSocial() + " excluída com sucesso.");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public void pesquisar() {
		model = new LazyDataModel<Empresa>() {
			private static final long serialVersionUID = 1L;
				
			@Override
			public List<Empresa> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setPropriedadeOrdenacao(sortField);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				
				setRowCount(empresas.quantidadeFiltrados(filtro));
				return empresas.filtradas(filtro);
			}
		};
	}
	
	public LazyDataModel<Empresa> getModel() {
		return model;
	}

	public EmpresaFilter getFiltro() {
		return filtro;
	}

	public Empresa getEmpresaSelecionada() {
		return empresaSelecionada;
	}

	public void setEmpresaSelecionada(Empresa empresaSelecionada) {
		this.empresaSelecionada = empresaSelecionada;
	}
}