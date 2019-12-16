package br.edu.ucv.estagio.repository.filter;

import java.io.Serializable;

import br.edu.ucv.estagio.model.Grau;

public class CursoFilter extends OrdenacaoFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private Grau grau;

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

	public Grau getGrau() {
		return grau;
	}

	public void setGrau(Grau grau) {
		this.grau = grau;
	}
}