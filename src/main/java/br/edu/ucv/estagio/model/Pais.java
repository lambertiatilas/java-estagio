package br.edu.ucv.estagio.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import br.edu.ucv.estagio.util.string.StringUtil;

@Entity
@Table(name = "pais")
public class Pais implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private String sigla;

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
	@Column(length = 80, nullable = false, unique = true)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = StringUtil.trimAll(nome);
	}

	@NotBlank
	@Size(max = 3)
	@Column(columnDefinition = "CHAR(3)", nullable = false, unique = true)
	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla.toUpperCase();
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
		Pais other = (Pais) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}