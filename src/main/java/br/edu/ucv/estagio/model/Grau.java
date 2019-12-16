package br.edu.ucv.estagio.model;

public enum Grau {

	TECNOLOGO("TECNÓLOGO"),
	BACHARELADO("BACHARELADO"),
	LICENCIATURA("LICENCIATURA");

	private String descricao;

	Grau(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}