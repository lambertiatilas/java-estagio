package br.edu.ucv.estagio.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import br.edu.ucv.estagio.util.string.StringUtil;

@Entity
@Table(name = "empresa_estagio")
public class EmpresaEstagio implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private Endereco endereco;
	private String sala;
	private String representanteLegal;
	private Contato contato;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotNull
	@OneToOne(cascade = CascadeType.ALL)
	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	@NotBlank
	@Size(max = 80)
	@Column(length = 80, nullable = false)
	public String getSala() {
		return sala;
	}

	public void setSala(String sala) {
		this.sala = sala;
	}

	@NotBlank
	@Size(max = 80)
	@Column(name = "representante_legal", length = 80, nullable = false)
	public String getRepresentanteLegal() {
		return representanteLegal;
	}

	public void setRepresentanteLegal(String representanteLegal) {
		this.representanteLegal = StringUtil.trimAll(representanteLegal);
	}

	@NotNull
	@OneToOne(cascade = CascadeType.ALL)
	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
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
		EmpresaEstagio other = (EmpresaEstagio) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}