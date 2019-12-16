package br.edu.ucv.estagio.model;

public enum DiaSemana {
	
	DOMINGO("DOMINGO"),
	SEGUNDA("SEGUNDA"),
	TERCA("TERÇA"),
	QUARTA("QUARTA"),
	QUINTA("QUINTA"),
	SEXTA("SEXTA"),
	SABADO("SÁBADO");

	private String descricao;

	DiaSemana(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}