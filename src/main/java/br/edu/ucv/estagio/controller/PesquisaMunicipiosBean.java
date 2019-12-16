package br.edu.ucv.estagio.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.edu.ucv.estagio.model.Municipio;
import br.edu.ucv.estagio.model.TabelaAuditoria;
import br.edu.ucv.estagio.repository.Municipios;
import br.edu.ucv.estagio.repository.filter.MunicipioFilter;
import br.edu.ucv.estagio.service.CadastroAuditoriaService;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaMunicipiosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Municipios municipios;
	
	@Inject
	private CadastroAuditoriaService cadastroAuditoriaService;
	
	private MunicipioFilter filtro;
	private LazyDataModel<Municipio> model;
	
	private Municipio municipioSelecionado;
	
	public PesquisaMunicipiosBean() {
		filtro = new MunicipioFilter();
	}
	
	public void excluir() {
		try {
			municipios.remover(municipioSelecionado);
			cadastroAuditoriaService.salvar(TabelaAuditoria.MUNICIPIO, municipioSelecionado.getId(), true);
			FacesUtil.addInfoMessage("Município " + municipioSelecionado.getNome() + " excluído com sucesso.");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public void pesquisar() {
		model = new LazyDataModel<Municipio>() {
			private static final long serialVersionUID = 1L;
				
			@Override
			public List<Municipio> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setPropriedadeOrdenacao(sortField);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				
				setRowCount(municipios.quantidadeFiltrados(filtro));
				return municipios.filtrados(filtro);
			}
		};
	}
	
	public LazyDataModel<Municipio> getModel() {
		return model;
	}

	public MunicipioFilter getFiltro() {
		return filtro;
	}

	public Municipio getMunicipioSelecionado() {
		return municipioSelecionado;
	}

	public void setMunicipioSelecionado(Municipio municipioSelecionado) {
		this.municipioSelecionado = municipioSelecionado;
	}
}