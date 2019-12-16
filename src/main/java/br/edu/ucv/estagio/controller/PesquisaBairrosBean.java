package br.edu.ucv.estagio.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.edu.ucv.estagio.model.Bairro;
import br.edu.ucv.estagio.model.TabelaAuditoria;
import br.edu.ucv.estagio.repository.Bairros;
import br.edu.ucv.estagio.repository.filter.BairroFilter;
import br.edu.ucv.estagio.service.CadastroAuditoriaService;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaBairrosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Bairros bairros;
	
	@Inject
	private CadastroAuditoriaService cadastroAuditoriaService;
	
	private BairroFilter filtro;
	private LazyDataModel<Bairro> model;
	
	private Bairro bairroSelecionado;
	
	public PesquisaBairrosBean() {
		filtro = new BairroFilter();
	}
	
	public void excluir() {
		try {
			bairros.remover(bairroSelecionado);
			cadastroAuditoriaService.salvar(TabelaAuditoria.BAIRRO, bairroSelecionado.getId(), true);
			FacesUtil.addInfoMessage("Bairro " + bairroSelecionado.getNome() + " excluído com sucesso.");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public void pesquisar() {
		model = new LazyDataModel<Bairro>() {
			private static final long serialVersionUID = 1L;
				
			@Override
			public List<Bairro> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setPropriedadeOrdenacao(sortField);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				
				setRowCount(bairros.quantidadeFiltrados(filtro));
				return bairros.filtrados(filtro);
			}
		};
	}
	
	public LazyDataModel<Bairro> getModel() {
		return model;
	}

	public BairroFilter getFiltro() {
		return filtro;
	}

	public Bairro getBairroSelecionado() {
		return bairroSelecionado;
	}

	public void setBairroSelecionado(Bairro bairroSelecionado) {
		this.bairroSelecionado = bairroSelecionado;
	}
}