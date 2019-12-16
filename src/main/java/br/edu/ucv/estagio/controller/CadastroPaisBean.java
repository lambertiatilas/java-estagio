package br.edu.ucv.estagio.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.edu.ucv.estagio.model.Pais;
import br.edu.ucv.estagio.model.TabelaAuditoria;
import br.edu.ucv.estagio.service.CadastroAuditoriaService;
import br.edu.ucv.estagio.service.CadastroPaisService;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroPaisBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private CadastroAuditoriaService cadastroAuditoriaService;
	
	@Inject
	private CadastroPaisService cadastroPaisService;

	private Pais pais;

	public void inicializar() {
		if (pais == null) {
			limpar();
		}
	}

	private void limpar() {
		pais = new Pais();
	}

	public void salvar() {
		try {
			pais = cadastroPaisService.salvar(pais);
			cadastroAuditoriaService.salvar(TabelaAuditoria.PAIS, pais.getId(), false);
			limpar();
			FacesUtil.addInfoMessage("Pais salvo com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public boolean isEditando() {
		return pais.getId() != null;
	}
}