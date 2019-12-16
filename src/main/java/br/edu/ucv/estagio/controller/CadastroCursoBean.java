package br.edu.ucv.estagio.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.edu.ucv.estagio.model.Curso;
import br.edu.ucv.estagio.model.Grau;
import br.edu.ucv.estagio.model.TabelaAuditoria;
import br.edu.ucv.estagio.service.CadastroAuditoriaService;
import br.edu.ucv.estagio.service.CadastroCursoService;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroCursoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CadastroAuditoriaService cadastroAuditoriaService;
	
	@Inject
	private CadastroCursoService cadastroCursoService;

	private Curso curso;

	public void inicializar() {
		if (curso == null) {
			limpar();
		}
	}

	private void limpar() {
		curso = new Curso();
	}

	public void salvar() {
		try {
			curso = cadastroCursoService.salvar(curso);
			cadastroAuditoriaService.salvar(TabelaAuditoria.CURSO, curso.getId(), false);
			limpar();
			FacesUtil.addInfoMessage("Curso salvo com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	
	public Grau[] getGraus() {
		return Grau.values();
	}

	public boolean isEditando() {
		return curso.getId() != null;
	}
}