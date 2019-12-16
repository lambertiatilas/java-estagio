package br.edu.ucv.estagio.model;

public enum Periodo {
	
	PRIMEIRO_PERIODO("PRIMEIRO PERÍODO"),
	SEGUNDO_PERIODO("SEGUNDO PERÍODO"),
	TERCEIRO_PERIODO("TERCEIRO PERÍODO"),
	QUARTO_PERIODO("QUARTO PERÍODO"),
	QUINTO_PERIODO("QUINTO PERÍODO"),
	SEXTO_PERIODO("SEXTO PERÍODO"),
	SETIMO_PERIODO("SÉTIMO PERÍODO"),
	OITAVO_PERIODO("OITAVO PERÍODO"),
	NONO_PERIODO("NOVO PERÍODO"),
	DECIMO_PERIODO("DÉCIMO PERÍODO");

	private String descricao;

	Periodo(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}	
}