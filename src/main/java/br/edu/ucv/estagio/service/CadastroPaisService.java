package br.edu.ucv.estagio.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.edu.ucv.estagio.model.Pais;
import br.edu.ucv.estagio.repository.Paises;
import br.edu.ucv.estagio.util.jpa.Transactional;

public class CadastroPaisService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Paises paises;
	
	@Transactional
	public Pais salvar(Pais pais) throws NegocioException {
		Pais paisExiste = paises.porNome(pais.getNome());
		
		if (paisExiste != null && !paisExiste.equals(pais)) {
			throw new NegocioException("O país " + pais.getNome() + " já esta cadastrado.");
		}
		
		paisExiste = paises.porSigla(pais.getSigla());
		
		if (paisExiste != null && !paisExiste.equals(pais)) {
			throw new NegocioException("Já existe um país cadastrado com a sigla informada.");
		}
		
		return paises.guardar(pais);
	}
}