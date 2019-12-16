package br.edu.ucv.estagio.repository.filter;

import java.io.Serializable;

public class MunicipioFilter extends OrdenacaoFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private String estado;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}