package br.edu.ucv.estagio.repository.filter;

import java.io.Serializable;

public class EmpresaFilter extends OrdenacaoFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String razaoSocial;
	private String nomeFantasia;
	private String cnpj;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
}