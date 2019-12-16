package br.edu.ucv.estagio.repository.filter;

import java.io.Serializable;

import br.edu.ucv.estagio.model.TipoClausula;

public class ClausulaFilter extends OrdenacaoFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private TipoClausula tipo;
	private Integer numeroClausula;
	private String titulo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoClausula getTipo() {
		return tipo;
	}

	public void setTipo(TipoClausula tipo) {
		this.tipo = tipo;
	}

	public Integer getNumeroClausula() {
		return numeroClausula;
	}

	public void setNumeroClausula(Integer numeroClausula) {
		this.numeroClausula = numeroClausula;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
}