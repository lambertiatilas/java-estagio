package br.edu.ucv.estagio.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.edu.ucv.estagio.model.Clausula;
import br.edu.ucv.estagio.repository.Clausulas;
import br.edu.ucv.estagio.util.jpa.Transactional;

public class CadastroClausulaService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Clausulas clausulas;
	
	@Transactional
	public Clausula salvar(Clausula clausula) throws NegocioException {
		Clausula clausulaExiste = clausulas.existe(clausula);
		
		if (clausulaExiste != null && !clausulaExiste.equals(clausula)) {
			throw new NegocioException("Já existe uma cláusula cadastrada com (tipo e número) informado.");
		}
		
		return clausulas.guardar(clausula);
	}
}