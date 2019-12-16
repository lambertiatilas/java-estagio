package br.edu.ucv.estagio.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import br.edu.ucv.estagio.util.string.StringUtil;

@Entity
@Table(name = "estado")
public class Estado implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private String uf;
	private Pais pais;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotBlank
	@Size(max = 80)
	@Column(length = 80, nullable = false)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = StringUtil.trimAll(nome);
	}
	
	@NotBlank
	@Size(max = 2)
	@Column(columnDefinition = "CHAR(2)", nullable = false)
	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf.toUpperCase();
	}
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "pais_id", nullable = false)
	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
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
		Estado other = (Estado) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}