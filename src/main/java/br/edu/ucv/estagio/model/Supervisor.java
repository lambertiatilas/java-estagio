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
@Table(name = "supervisor")
public class Supervisor implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private String cargo;
	private String formacao;
	private String registroProfissional;

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
	@Size(max = 80)
	@Column(length = 80, nullable = false)
	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = StringUtil.trimAll(cargo);
	}

	@NotBlank
	@Size(max = 80)
	@Column(length = 80, nullable = false)
	public String getFormacao() {
		return formacao;
	}

	public void setFormacao(String formacao) {
		this.formacao = StringUtil.trimAll(formacao);
	}

	@Size(max = 20)
	@Column(name = "registro_profissional", length = 20)
	public String getRegistroProfissional() {
		return registroProfissional;
	}

	public void setRegistroProfissional(String registroProfissional) {
		this.registroProfissional = StringUtil.trimAll(registroProfissional);
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
		Supervisor other = (Supervisor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}