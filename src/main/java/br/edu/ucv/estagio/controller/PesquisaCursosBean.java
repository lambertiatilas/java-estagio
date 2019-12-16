package br.edu.ucv.estagio.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.edu.ucv.estagio.model.Curso;
import br.edu.ucv.estagio.model.Grau;
import br.edu.ucv.estagio.model.TabelaAuditoria;
import br.edu.ucv.estagio.repository.Cursos;
import br.edu.ucv.estagio.repository.filter.CursoFilter;
import br.edu.ucv.estagio.service.CadastroAuditoriaService;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaCursosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Cursos cursos;
	
	@Inject
	private CadastroAuditoriaService cadastroAuditoriaService;
	
	private CursoFilter filtro;
	private LazyDataModel<Curso> model;
	
	private Curso cursoSelecionado;
	
	public PesquisaCursosBean() {
		filtro = new CursoFilter();
	}
	
	public void excluir() {
		try {
			cursos.remover(cursoSelecionado);
			cadastroAuditoriaService.salvar(TabelaAuditoria.CURSO, cursoSelecionado.getId(), true);
			FacesUtil.addInfoMessage("Curso " + cursoSelecionado.getNome() + " excluído com sucesso.");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public void pesquisar() {
		model = new LazyDataModel<Curso>() {
			private static final long serialVersionUID = 1L;
				
			@Override
			public List<Curso> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setPropriedadeOrdenacao(sortField);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				
				setRowCount(cursos.quantidadeFiltrados(filtro));
				return cursos.filtrados(filtro);
			}
		};
	}
	
	public LazyDataModel<Curso> getModel() {
		return model;
	}

	public CursoFilter getFiltro() {
		return filtro;
	}

	public Curso getCursoSelecionado() {
		return cursoSelecionado;
	}

	public void setCursoSelecionado(Curso cursoSelecionado) {
		this.cursoSelecionado = cursoSelecionado;
	}
	
	public Grau[] getGraus() {
		return Grau.values();
	}
}