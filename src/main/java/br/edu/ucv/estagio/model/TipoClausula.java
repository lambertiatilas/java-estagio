package br.edu.ucv.estagio.model;

public enum TipoClausula {
	
	TCRL("TERMO DE COMPROMISSO DE ESTÁGIO: REFERÊNCIA LEGAL"),
	TCDE("TERMO DE COMPROMISSO DE ESTÁGIO: DESCRIÇÃO"),
	TCNC("TERMO DE COMPROMISSO DE ESTÁGIO (NÃO OBRIGATÓRIO): CLÁUSULA"),
	TCVC("TERMO DE COMPROMISSO DE ESTÁGIO (VOLUNTÁRIO): CLÁUSULA"),
	TACD("TERMO ADITIVO AO TERMO DE COMPROMISSO DE ESTÁGIO: DESCRIÇÃO"),
	TACC("TERMO ADITIVO AO TERMO DE COMPROMISSO DE ESTÁGIO: CLÁUSULA"),
	TDCD("TERMO DE DISTRATO DE ESTÁGIO: DESCRIÇÃO");

	private String descricao;

	TipoClausula(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}