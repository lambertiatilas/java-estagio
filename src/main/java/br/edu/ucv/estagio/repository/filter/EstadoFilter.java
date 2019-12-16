package br.edu.ucv.estagio.repository.filter;

import java.io.Serializable;

public class EstadoFilter extends OrdenacaoFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private String uf;
	private String pais;

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

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}
}