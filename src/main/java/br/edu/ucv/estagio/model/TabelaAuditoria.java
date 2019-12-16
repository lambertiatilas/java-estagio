package br.edu.ucv.estagio.model;

public enum TabelaAuditoria {
	
	BAIRRO("BAIRRO"),
	CLAUSULA("CLÁUSULA"),
	CURSO("CURSO"),
	EMPRESA("EMPRESA"),
	ESTADO("ESTADO"),
	ESTAGIO("ESTÁGIO"),
	INSTITUICAO("INSTITUIÇÃO"),
	MUNICIPIO("MUNICÍPIO"),
	PAIS("PAÍS"),
	PESSOA("PESSOA");

	private String descricao;

	TabelaAuditoria(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}