package br.edu.ucv.estagio.service;

import java.io.Serializable;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import br.edu.ucv.estagio.model.Pessoa;
import br.edu.ucv.estagio.repository.Usuarios;
import br.edu.ucv.estagio.util.jpa.Transactional;

public class CadastroUsuarioService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	protected Usuarios usuarios;
	
	@Transactional
	public Pessoa salvar(Pessoa pessoa) throws NegocioException {
		verificaPessoaExiste(pessoa);
		
		if (pessoa.isNova()) {
			pessoa.getUsuario().setSenha(pessoa.gerarSenhaUsuario());
		}
		
		return usuarios.guardar(pessoa);
	}
	
	protected void verificaPessoaExiste(Pessoa pessoa) throws NegocioException {
		Pessoa pessoaExiste = usuarios.porCpf(pessoa.getCpf());
		
		if (pessoaExiste != null && !pessoaExiste.equals(pessoa)) {
			throw new NegocioException("Já existe uma pessoa cadastrada com o cpf informado.");
		}
		
		if (StringUtils.isNotBlank(pessoa.getContato().getEmail())) {
			pessoaExiste = usuarios.porEmail(pessoa.getContato().getEmail());
			
			if (pessoaExiste != null && !pessoaExiste.equals(pessoa)) {
				throw new NegocioException("Já existe uma pessoa cadastrada com o email informado.");
			}
		}
	}	
}