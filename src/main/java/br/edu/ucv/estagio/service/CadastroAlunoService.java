package br.edu.ucv.estagio.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.edu.ucv.estagio.model.Pessoa;
import br.edu.ucv.estagio.model.TipoPessoa;
import br.edu.ucv.estagio.repository.Alunos;
import br.edu.ucv.estagio.repository.Grupos;
import br.edu.ucv.estagio.util.jpa.Transactional;

public class CadastroAlunoService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Grupos grupos;
	
	@Inject
	private Alunos alunos;
	
	@Inject
	private CadastroUsuarioService cadastroPessoaService;
	
	@Transactional
	public Pessoa salvar(Pessoa pessoa) throws NegocioException {
		cadastroPessoaService.verificaPessoaExiste(pessoa);
		
		if (pessoa.isNova()) {
			pessoa.setTipo(TipoPessoa.ALUNO);
			pessoa.getUsuario().setSenha(pessoa.gerarSenhaUsuario());
			pessoa.getUsuario().getGrupos().add(grupos.porNome("ALUNOS"));
		}
		
		return alunos.guardar(pessoa);
	}
}