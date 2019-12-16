package br.edu.ucv.estagio.model;

public enum TipoPessoa {
	
	ALUNO("ALUNO"),
	FUNCIONARIO("FUNCIONÁRIO");

	private String descricao;

	TipoPessoa(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}