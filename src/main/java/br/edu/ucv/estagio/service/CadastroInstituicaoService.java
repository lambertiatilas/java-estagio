package br.edu.ucv.estagio.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.edu.ucv.estagio.model.Instituicao;
import br.edu.ucv.estagio.repository.Instituicoes;
import br.edu.ucv.estagio.util.jpa.Transactional;

public class CadastroInstituicaoService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Instituicoes instituicoes;
	
	@Transactional
	public Instituicao salvar(Instituicao instituicao) throws NegocioException {
		Instituicao instituicaoExiste = instituicoes.porNome(instituicao.getNome());
		
		if (instituicaoExiste != null && !instituicaoExiste.equals(instituicao)) {
			throw new NegocioException("A instituição " + instituicao.getNome() + " já esta cadastrada.");
		}
		
		return instituicoes.guardar(instituicao);
	}
}