package br.edu.ucv.estagio.controller;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.edu.ucv.estagio.model.Municipio;
import br.edu.ucv.estagio.model.TabelaAuditoria;
import br.edu.ucv.estagio.service.CadastroAuditoriaService;
import br.edu.ucv.estagio.service.CadastroMunicipioService;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroMunicipioBean extends CadastroComEnderecoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private CadastroAuditoriaService cadastroAuditoriaService;
	
	@Inject
	private CadastroMunicipioService cadastroMunicipioService;
	
	private Municipio municipio;

	public void inicializar() {
		if (municipio == null) {
			limpar();
		}
		
		iniciarMunicipios(municipio);
	}

	private void limpar() {
		municipio = new Municipio();
		municipio.setEstado(novoEstado());
	}

	public void salvar() {
		try {
			municipio = cadastroMunicipioService.salvar(municipio);
			cadastroAuditoriaService.salvar(TabelaAuditoria.MUNICIPIO, municipio.getId(), false);
			limpar();
			FacesUtil.addInfoMessage("Município salvo com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}
	
	public boolean isEditando() {
		return municipio.getId() != null;
	}
}