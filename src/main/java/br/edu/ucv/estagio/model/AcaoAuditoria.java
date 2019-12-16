package br.edu.ucv.estagio.model;

public enum AcaoAuditoria {
	
	INSERCAO("INSERÇÃO"),
	ATUALIZACAO("ATUALIZAÇÃO"),
	EXCLUSAO("EXCLUSÃO");

	private String descricao;

	AcaoAuditoria(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}