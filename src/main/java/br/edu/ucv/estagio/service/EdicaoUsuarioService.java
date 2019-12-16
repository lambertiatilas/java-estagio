package br.edu.ucv.estagio.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.edu.ucv.estagio.model.Pessoa;
import br.edu.ucv.estagio.model.Status;
import br.edu.ucv.estagio.repository.Usuarios;
import br.edu.ucv.estagio.util.jpa.Transactional;

public class EdicaoUsuarioService implements Serializable {
	
private static final long serialVersionUID = 1L;
	
	@Inject
	private Usuarios usuarios;
	
	@Transactional
	public Pessoa alterarStatusUsuario(Pessoa pessoa) throws NegocioException {
		pessoa = usuarios.porId(pessoa.getId());
		
		if (pessoa.isUsuarioNaoExistente()) {
			throw new NegocioException("O status não pode ser alterado, poque " + pessoa.getNome() + " não possue usuário de acesso ao sistema!");
		}
		
		if (pessoa.getUsuario().isAtivo()) {
			pessoa.getUsuario().setStatus(Status.INATIVO);
		} else {
			pessoa.getUsuario().setStatus(Status.ATIVO);
		}
		
		return usuarios.guardar(pessoa);
	}
	
	@Transactional
	public Pessoa resetarSenhaUsuario(Pessoa pessoa) throws NegocioException {
		pessoa = usuarios.porId(pessoa.getId());
		
		if (pessoa.isUsuarioNaoExistente()) {
			throw new NegocioException("A senha não pode ser resetada, poque " + pessoa.getNome() + " não possue usuário de acesso ao sistema!");
		}
		
		pessoa.getUsuario().setSenha(pessoa.gerarSenhaUsuario());;
		return usuarios.guardar(pessoa);
	}
}