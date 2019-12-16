package br.edu.ucv.estagio.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.edu.ucv.estagio.util.string.StringUtil;

@Entity
@Table(name = "arquivo")
public class Arquivo implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private byte[] conteudo;
	private Estagio estagio;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(length = 80, nullable = false)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = StringUtil.trimAll(nome);
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	public byte[] getConteudo() {
		return conteudo;
	}

	public void setConteudo(byte[] conteudo) {
		this.conteudo = conteudo;
	}

	@ManyToOne
	@JoinColumn(name = "estagio_id", nullable = false)
	public Estagio getEstagio() {
		return estagio;
	}

	public void setEstagio(Estagio estagio) {
		this.estagio = estagio;
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
		Arquivo other = (Arquivo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}