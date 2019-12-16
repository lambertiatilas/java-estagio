package br.edu.ucv.estagio.repository.filter;

import java.io.Serializable;
import java.util.Date;

import br.edu.ucv.estagio.model.Status;

public class EstagioFilter extends OrdenacaoFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Date dataInicioDe;
	private Date dataInicioAte;
	private Date dataFimDe;
	private Date dataFimAte;
	private String nomeAluno;
	private Long matriculaAluno;
	private String razaoSocialEmpresa;
	private String cnpjEmpresa;
	private Status status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataInicioDe() {
		return dataInicioDe;
	}

	public void setDataInicioDe(Date dataInicioDe) {
		this.dataInicioDe = dataInicioDe;
	}

	public Date getDataInicioAte() {
		return dataInicioAte;
	}

	public void setDataInicioAte(Date dataInicioAte) {
		this.dataInicioAte = dataInicioAte;
	}

	public Date getDataFimDe() {
		return dataFimDe;
	}

	public void setDataFimDe(Date dataFimDe) {
		this.dataFimDe = dataFimDe;
	}

	public Date getDataFimAte() {
		return dataFimAte;
	}

	public void setDataFimAte(Date dataFimAte) {
		this.dataFimAte = dataFimAte;
	}

	public String getNomeAluno() {
		return nomeAluno;
	}

	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}

	public Long getMatriculaAluno() {
		return matriculaAluno;
	}

	public void setMatriculaAluno(Long matriculaAluno) {
		this.matriculaAluno = matriculaAluno;
	}

	public String getRazaoSocialEmpresa() {
		return razaoSocialEmpresa;
	}

	public void setRazaoSocialEmpresa(String razaoSocialEmpresa) {
		this.razaoSocialEmpresa = razaoSocialEmpresa;
	}

	public String getCnpjEmpresa() {
		return cnpjEmpresa;
	}

	public void setCnpjEmpresa(String cnpjEmpresa) {
		this.cnpjEmpresa = cnpjEmpresa;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}