package br.edu.ucv.estagio.controller;

import br.edu.ucv.estagio.model.Pessoa;

public class EventPessoaAlterada {
	
	private Pessoa pessoa;
	
	public EventPessoaAlterada(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}
}