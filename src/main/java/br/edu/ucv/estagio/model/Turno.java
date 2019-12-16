package br.edu.ucv.estagio.model;

public enum Turno {
	
	MATUTINO("MATUTINO"),
	VESPERTINO("VESPERTINO"),
	NOTURNO("NOTURNO");

	private String descricao;

	Turno(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}