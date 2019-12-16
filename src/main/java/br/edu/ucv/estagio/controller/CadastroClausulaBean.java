package br.edu.ucv.estagio.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.edu.ucv.estagio.model.Clausula;
import br.edu.ucv.estagio.model.TabelaAuditoria;
import br.edu.ucv.estagio.model.TipoClausula;
import br.edu.ucv.estagio.service.CadastroAuditoriaService;
import br.edu.ucv.estagio.service.CadastroClausulaService;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroClausulaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private CadastroAuditoriaService cadastroAuditoriaService;
	
	@Inject
	private CadastroClausulaService cadastroClausulaService;

	private Clausula clausula;

	public void inicializar() {
		if (clausula == null) {
			limpar();
		}
	}

	private void limpar() {
		clausula = new Clausula();
	}

	public void salvar() {
		try {
			clausula = cadastroClausulaService.salvar(clausula);
			cadastroAuditoriaService.salvar(TabelaAuditoria.CLAUSULA, clausula.getId(), false);
			limpar();
			FacesUtil.addInfoMessage("Cláusula salva com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public Clausula getClausula() {
		return clausula;
	}

	public void setClausula(Clausula clausula) {
		this.clausula = clausula;
	}
	
	public TipoClausula[] getTipos() {
		return TipoClausula.values();
	}

	public boolean isEditando() {
		return clausula.getId() != null;
	}
}