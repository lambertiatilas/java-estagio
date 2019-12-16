package br.edu.ucv.estagio.model;

public enum TipoEstagio {
	
	OBRIGATORIO("OBRIGATÓRIO"),
	NAO_OBRIGATORIO("NÃO OBRIGATÓRIO"),
	VOLUNTARIO("VOLUNTÁRIO");

	private String descricao;

	TipoEstagio(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}