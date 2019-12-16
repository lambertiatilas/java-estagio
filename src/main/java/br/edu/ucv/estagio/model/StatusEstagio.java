package br.edu.ucv.estagio.model;

public enum StatusEstagio {
	
	PENDENTE("PENDENTE"),
	ATIVO("ATIVO"),
	FINALIZADO("FINALIZADO");

	private String descricao;

	StatusEstagio(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}