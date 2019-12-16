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
import br.edu.ucv.estagio.model.Status;
import br.edu.ucv.estagio.model.TabelaAuditoria;
import br.edu.ucv.estagio.model.TipoPessoa;
import br.edu.ucv.estagio.repository.Usuarios;
import br.edu.ucv.estagio.repository.filter.UsuarioFilter;
import br.edu.ucv.estagio.service.CadastroAuditoriaService;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaUsuariosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Usuarios usuarios;
	
	@Inject
	private CadastroAuditoriaService cadastroAuditoriaService;
	
	private UsuarioFilter filtro;
	private LazyDataModel<Pessoa> model;
	
	private Pessoa pessoaSelecionada;
	
	public PesquisaUsuariosBean() {
		filtro = new UsuarioFilter();
	}
	
	public void excluir() {
		try {
			usuarios.remover(pessoaSelecionada);
			cadastroAuditoriaService.salvar(TabelaAuditoria.PESSOA, pessoaSelecionada.getId(), true);
			FacesUtil.addInfoMessage("Usuário " + pessoaSelecionada.getNome() + " excluído com sucesso.");
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
				
				setRowCount(usuarios.quantidadeFiltrados(filtro));
				return usuarios.filtrados(filtro);
			}
		};
	}
	
	public LazyDataModel<Pessoa> getModel() {
		return model;
	}

	public UsuarioFilter getFiltro() {
		return filtro;
	}

	public void setFiltro(UsuarioFilter filtro) {
		this.filtro = filtro;
	}

	public Pessoa getPessoaSelecionada() {
		return pessoaSelecionada;
	}

	public void setPessoaSelecionada(Pessoa pessoaSelecionada) {
		this.pessoaSelecionada = pessoaSelecionada;
	}
	
	public TipoPessoa[] getTipos() {
		return TipoPessoa.values();
	}
	
	public Status[] getStatuses() {
		return Status.values();
	}
}