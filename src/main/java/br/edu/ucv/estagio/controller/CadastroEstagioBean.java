package br.edu.ucv.estagio.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.edu.ucv.estagio.model.Curso;
import br.edu.ucv.estagio.model.DiaSemana;
import br.edu.ucv.estagio.model.Empresa;
import br.edu.ucv.estagio.model.Instituicao;
import br.edu.ucv.estagio.model.Pessoa;
import br.edu.ucv.estagio.model.Turno;
import br.edu.ucv.estagio.repository.Cursos;
import br.edu.ucv.estagio.repository.Empresas;
import br.edu.ucv.estagio.repository.Instituicoes;
import br.edu.ucv.estagio.service.CadastroEstagioService;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroEstagioBean extends CadastroComEnderecoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Cursos cursos;

	@Inject
	private Empresas empresas;
	
	@Inject
	private Instituicoes instituicoes;

	@Inject
	private CadastroEstagioService cadastroEstagioService;

	private Pessoa pessoa;

	private List<Curso> listaCursos = new ArrayList<>();
	private List<Empresa> listaEmpresas = new ArrayList<>();
	private List<Instituicao> listaInstituicoes = new ArrayList<>();

	public void inicializar() {
		if (pessoa == null) {
			FacesUtil.addErrorMessage("Usuário não encontrado!");
		}
		
		listaCursos = cursos.cursos();
		listaEmpresas = empresas.empresas();
		listaInstituicoes = instituicoes.instituicoes();
		carregarNovoEstagio();
		iniciarEnderecos(pessoa.getEstagiosNaoFinalizados().get(0).getEmpresaEstagio().getEndereco());
	}

	public void salvar(Long id) {
		pessoa.removerItemVazio();
		
		try {
			pessoa = cadastroEstagioService.salvar(pessoa, id);
			carregarNovoEstagio();
			FacesUtil.addInfoMessage("Estágio salvo com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public List<Curso> getListaCursos() {
		return listaCursos;
	}

	public List<Empresa> getListaEmpresas() {
		return listaEmpresas;
	}

	public List<Instituicao> getListaInstituicoes() {
		return listaInstituicoes;
	}

	public Turno[] getTurnos() {
		return Turno.values();
	}

	public DiaSemana[] getDiasSemana() {
		return DiaSemana.values();
	}
	
	private void carregarNovoEstagio() {
		if (pessoa.cargaHorariaEstagiosNaoFinalizados() < CadastroEstagioService.maximaCargaHoraria) {
			pessoa.adicionarEstagioVazio(novoEndereco());
		}
	}
}