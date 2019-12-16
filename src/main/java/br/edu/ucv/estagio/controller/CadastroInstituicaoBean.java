package br.edu.ucv.estagio.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.edu.ucv.estagio.model.Instituicao;
import br.edu.ucv.estagio.model.TabelaAuditoria;
import br.edu.ucv.estagio.service.CadastroAuditoriaService;
import br.edu.ucv.estagio.service.CadastroInstituicaoService;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroInstituicaoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private CadastroAuditoriaService cadastroAuditoriaService;
	
	@Inject
	private CadastroInstituicaoService cadastroInstituicaoService;

	private Instituicao instituicao;

	public void inicializar() {
		if (instituicao == null) {
			limpar();
		}
	}

	private void limpar() {
		instituicao = new Instituicao();
	}

	public void salvar() {
		try {
			instituicao = cadastroInstituicaoService.salvar(instituicao);
			cadastroAuditoriaService.salvar(TabelaAuditoria.INSTITUICAO, instituicao.getId(), false);
			limpar();
			FacesUtil.addInfoMessage("Instituição salva com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}

	public Instituicao getInstituicao() {
		return instituicao;
	}

	public void setInstituicao(Instituicao instituicao) {
		this.instituicao = instituicao;
	}

	public boolean isEditando() {
		return instituicao.getId() != null;
	}
}