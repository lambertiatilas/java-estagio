package br.edu.ucv.estagio.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.edu.ucv.estagio.model.Municipio;
import br.edu.ucv.estagio.repository.Municipios;
import br.edu.ucv.estagio.util.jpa.Transactional;

public class CadastroMunicipioService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Municipios municipios;
	
	@Transactional
	public Municipio salvar(Municipio municipio) throws NegocioException {
		Municipio municipioExiste = municipios.porNome(municipio);
		
		if (municipioExiste != null && !municipioExiste.equals(municipio)) {
			throw new NegocioException("O município " + municipio.getNome() + " já esta cadastrado.");
		}
		
		return municipios.guardar(municipio);
	}
}