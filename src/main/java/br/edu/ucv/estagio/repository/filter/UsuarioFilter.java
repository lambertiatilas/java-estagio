package br.edu.ucv.estagio.repository.filter;

import java.io.Serializable;

import br.edu.ucv.estagio.model.Status;
import br.edu.ucv.estagio.model.TipoPessoa;

public class UsuarioFilter extends PessoaFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private TipoPessoa[] tipos;
	private Status[] statuses = {Status.ATIVO};

	public TipoPessoa[] getTipos() {
		return tipos;
	}

	public void setTipos(TipoPessoa[] tipos) {
		this.tipos = tipos;
	}

	public Status[] getStatuses() {
		return statuses;
	}

	public void setStatuses(Status[] statuses) {
		this.statuses = statuses;
	}
}