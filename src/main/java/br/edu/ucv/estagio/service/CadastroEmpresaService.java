package br.edu.ucv.estagio.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.edu.ucv.estagio.model.Empresa;
import br.edu.ucv.estagio.repository.Empresas;
import br.edu.ucv.estagio.util.jpa.Transactional;

public class CadastroEmpresaService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Empresas empresas;
	
	@Transactional
	public Empresa salvar(Empresa empresa) throws NegocioException {
		Empresa empresaExiste = empresas.porRazaoSocial(empresa.getRazaoSocial());
		
		if (empresaExiste != null && !empresaExiste.equals(empresa)) {
			throw new NegocioException("A empresa " + empresa.getRazaoSocial() + " já esta cadastrada.");
		}
		
		empresaExiste = empresas.porCnpj(empresa.getCnpj());
		
		if (empresaExiste != null && !empresaExiste.equals(empresa)) {
			throw new NegocioException("Já existe uma empresa cadastrada com o CNPJ informado.");
		}
		
		return empresas.guardar(empresa);
	}
}