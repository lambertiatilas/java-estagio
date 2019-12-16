package br.edu.ucv.estagio.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.edu.ucv.estagio.model.Bairro;
import br.edu.ucv.estagio.repository.Bairros;
import br.edu.ucv.estagio.util.jpa.Transactional;

public class CadastroBairroService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Bairros bairros;
	
	@Transactional
	public Bairro salvar(Bairro bairro) throws NegocioException {
		Bairro bairroExiste = bairros.porNome(bairro);
		
		if (bairroExiste != null && !bairroExiste.equals(bairro)) {
			throw new NegocioException("O bairro " + bairro.getNome() + " já esta cadastrado.");
		}
		
		return bairros.guardar(bairro);
	}
}