package br.edu.ucv.estagio.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.edu.ucv.estagio.model.Estado;
import br.edu.ucv.estagio.repository.Estados;
import br.edu.ucv.estagio.util.jpa.Transactional;

public class CadastroEstadoService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Estados estados;
	
	@Transactional
	public Estado salvar(Estado estado) throws NegocioException {
		Estado estadoExiste = estados.porNome(estado);
		
		if (estadoExiste != null && !estadoExiste.equals(estado)) {
			throw new NegocioException("O estado " + estado.getNome() + " já esta cadastrado.");
		}
		
		estadoExiste = estados.porUf(estado);
		
		if (estadoExiste != null && !estadoExiste.equals(estado)) {
			throw new NegocioException("Já existe um estado cadastrado com a UF informada.");
		}
		
		return estados.guardar(estado);
	}
}