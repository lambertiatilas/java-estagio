package br.edu.ucv.estagio.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "estagio")
public class Estagio implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Empresa empresa;
	private EmpresaEstagio empresaEstagio;
	private Pessoa pessoa;
	private PessoaEstagio pessoaEstagio;
	private Instituicao instituicao;
	private List<Contrato> contratos = new ArrayList<>();
	private Acompanhamento acompanhamento;
	private List<Arquivo> arquivos = new ArrayList<>();
	private StatusEstagio status;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotNull
	@ManyToOne
	@JoinColumn(name = "empresa_id", nullable = false)
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	@OneToOne(cascade = CascadeType.ALL)
	public EmpresaEstagio getEmpresaEstagio() {
		return empresaEstagio;
	}

	public void setEmpresaEstagio(EmpresaEstagio empresaEstagio) {
		this.empresaEstagio = empresaEstagio;
	}

	@ManyToOne
	@JoinColumn(name = "pessoa_id", nullable = false)
	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	@OneToOne(cascade = CascadeType.ALL)
	public PessoaEstagio getPessoaEstagio() {
		return pessoaEstagio;
	}

	public void setPessoaEstagio(PessoaEstagio pessoaEstagio) {
		this.pessoaEstagio = pessoaEstagio;
	}

	@NotNull
	@ManyToOne
	@JoinColumn(name = "instituicao_id", nullable = false)
	public Instituicao getInstituicao() {
		return instituicao;
	}

	public void setInstituicao(Instituicao instituicao) {
		this.instituicao = instituicao;
	}

	@OneToMany(mappedBy = "estagio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	public List<Contrato> getContratos() {
		return contratos;
	}

	public void setContratos(List<Contrato> contratos) {
		this.contratos = contratos;
	}

	@OneToOne(cascade = CascadeType.ALL)
	public Acompanhamento getAcompanhamento() {
		return acompanhamento;
	}

	public void setAcompanhamento(Acompanhamento acompanhamento) {
		this.acompanhamento = acompanhamento;
	}

	@OneToMany(mappedBy = "estagio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	public List<Arquivo> getArquivos() {
		return arquivos;
	}

	public void setArquivos(List<Arquivo> arquivos) {
		this.arquivos = arquivos;
	}

	@Enumerated
	@Column(nullable = false)
	public StatusEstagio getStatus() {
		return status;
	}

	public void setStatus(StatusEstagio status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Estagio other = (Estagio) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Transient
	public boolean isNovo() {
		return id == null;
	}
	
	@Transient
	public boolean isExistente() {
		return !isNovo();
	}
	
	@Transient
	private boolean isPendente() {
		return StatusEstagio.PENDENTE.equals(status);
	}
	
	@Transient
	private boolean isAtivo() {
		return StatusEstagio.ATIVO.equals(status);
	}
	
	@Transient
	private boolean isFinalizado() {
		return StatusEstagio.FINALIZADO.equals(status);
	}
	
	@Transient
	public boolean isNaoFinalizado() {
		return !isFinalizado();
	}
	
	@Transient
	public boolean isAlteravel() {
		return isExistente() && (isPendente() || isAtivo());
	}
	
	@Transient
	public boolean isFinalizavel() {
		return isAtivo();
	}
	
	private Contrato novoContrato() {
		Contrato contrato = new Contrato();
		contrato.setSupervisor(new Supervisor());
		contrato.setEstagio(this);
		return contrato;
	}
	
	public void adcionarNovoContrato() {
		getContratos().add(novoContrato());
	}
	
	@Transient
	public Contrato getContrato() {
		Contrato contrato = novoContrato();
		
		if (!contratos.isEmpty()) {
			contrato = contratos.get(contratos.size() - 1);
		}
		
		return contrato;
	}
	
	@Transient
	public String getAbaTitulo() {
		if (isNovo()) {
			return "Novo estágio";
		}
		
		return id + "º. " + "Estagio (" + empresa.getNomeFantasia() + ")";
	}
}