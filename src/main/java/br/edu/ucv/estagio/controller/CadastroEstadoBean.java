package br.edu.ucv.estagio.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.edu.ucv.estagio.model.Estado;
import br.edu.ucv.estagio.model.TabelaAuditoria;
import br.edu.ucv.estagio.service.CadastroAuditoriaService;
import br.edu.ucv.estagio.service.CadastroEstadoService;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroEstadoBean extends CadastroComEnderecoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private CadastroAuditoriaService cadastroAuditoriaService;
	
	@Inject
	private CadastroEstadoService cadastroEstadoService;

	private Estado estado;

	public void inicializar() {
		if (estado == null) {
			limpar();
		}
		
		iniciarEstados();
	} 

	private void limpar() {
		estado = new Estado();
		estado.setPais(novoPais());
	}

	public void salvar() {
		try {
			estado = cadastroEstadoService.salvar(estado);
			cadastroAuditoriaService.salvar(TabelaAuditoria.ESTADO, estado.getId(), false);
			limpar();
			FacesUtil.addInfoMessage("Estado salvo com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	
	public boolean isEditando() {
		return estado.getId() != null;
	}
}