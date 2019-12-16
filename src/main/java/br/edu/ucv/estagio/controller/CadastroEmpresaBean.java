package br.edu.ucv.estagio.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.edu.ucv.estagio.model.Empresa;
import br.edu.ucv.estagio.model.TabelaAuditoria;
import br.edu.ucv.estagio.service.CadastroAuditoriaService;
import br.edu.ucv.estagio.service.CadastroEmpresaService;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroEmpresaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private CadastroAuditoriaService cadastroAuditoriaService;
	
	@Inject
	private CadastroEmpresaService cadastroEmpresaService;

	private Empresa empresa;

	public void inicializar() {
		if (empresa == null) {
			limpar();
		}
	}

	private void limpar() {
		empresa = new Empresa();
	}

	public void salvar() {
		try {
			empresa = cadastroEmpresaService.salvar(empresa);
			cadastroAuditoriaService.salvar(TabelaAuditoria.EMPRESA, empresa.getId(), false);
			limpar();
			FacesUtil.addInfoMessage("Empresa salva com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public boolean isEditando() {
		return empresa.getId() != null;
	}
}