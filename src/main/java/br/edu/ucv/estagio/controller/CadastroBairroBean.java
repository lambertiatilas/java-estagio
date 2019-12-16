package br.edu.ucv.estagio.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.edu.ucv.estagio.model.Bairro;
import br.edu.ucv.estagio.model.TabelaAuditoria;
import br.edu.ucv.estagio.service.CadastroAuditoriaService;
import br.edu.ucv.estagio.service.CadastroBairroService;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroBairroBean extends CadastroComEnderecoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private CadastroAuditoriaService cadastroAuditoriaService;
	
	@Inject
	private CadastroBairroService cadastroBairroService;

	private Bairro bairro;
	
	public void inicializar() {
		if (bairro == null) {
			limpar();
		}
		
		iniciarBairros(bairro);
	}
	
	private void limpar() {
		bairro = new Bairro();
		bairro.setMunicipio(novoMunicipio());
	}

	public void salvar() {
		try {
			bairro = cadastroBairroService.salvar(bairro);
			cadastroAuditoriaService.salvar(TabelaAuditoria.BAIRRO, bairro.getId(), false);
			limpar();	
			FacesUtil.addInfoMessage("Bairro salvo com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public Bairro getBairro() {
		return bairro;
	}

	public void setBairro(Bairro bairro) {
		this.bairro = bairro;
	}
}