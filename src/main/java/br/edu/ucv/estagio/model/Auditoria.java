package br.edu.ucv.estagio.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "auditoria")
public class Auditoria implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private TabelaAuditoria tabelaNome;
	private Long tabelaId;
	private AcaoAuditoria acao;
	private Date dataCriacao;
	private Pessoa criadoPor;
	private Date dataModificacao;
	private Pessoa modificadoPor;
	private Long quantidadeSalva;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Enumerated
	@Column(name = "tabela_nome", nullable = false)
	public TabelaAuditoria getTabelaNome() {
		return tabelaNome;
	}

	public void setTabelaNome(TabelaAuditoria tabelaNome) {
		this.tabelaNome = tabelaNome;
	}

	@Column(name = "tabela_id", nullable = false)
	public Long getTabelaId() {
		return tabelaId;
	}

	public void setTabelaId(Long tabelaId) {
		this.tabelaId = tabelaId;
	}

	@Enumerated
	@Column(nullable = false)
	public AcaoAuditoria getAcao() {
		return acao;
	}

	public void setAcao(AcaoAuditoria acao) {
		this.acao = acao;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_criacao", nullable = false)
	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	@ManyToOne
	@JoinColumn(name = "criado_por_id")
	public Pessoa getCriadoPor() {
		return criadoPor;
	}

	public void setCriadoPor(Pessoa criadoPor) {
		this.criadoPor = criadoPor;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_modificacao", nullable = false)
	public Date getDataModificacao() {
		return dataModificacao;
	}

	public void setDataModificacao(Date dataModificacao) {
		this.dataModificacao = dataModificacao;
	}

	@ManyToOne
	@JoinColumn(name = "modificado_por_id")
	public Pessoa getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(Pessoa modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	@Column(name = "quantidade_salva", nullable = false)
	public Long getQuantidadeSalva() {
		return quantidadeSalva;
	}

	public void setQuantidadeSalva(Long quantidadeSalva) {
		this.quantidadeSalva = quantidadeSalva;
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
		Auditoria other = (Auditoria) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Transient
	public boolean isAtualizacao() {
		return AcaoAuditoria.ATUALIZACAO.equals(acao);
	}
}