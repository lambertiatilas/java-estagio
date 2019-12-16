package br.edu.ucv.estagio.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import br.edu.ucv.estagio.util.string.StringUtil;

@Entity
@Table(name = "curso")
public class Curso implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private Grau grau;
	private Integer quantidadePeriodo;

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

	@NotNull
	@Enumerated
	@Column(nullable = false)
	public Grau getGrau() {
		return grau;
	}

	public void setGrau(Grau grau) {
		this.grau = grau;
	}

	@NotNull
	@Min(value = 3)
	@Max(value = 10)
	@Column(name = "quantidade_periodo", nullable = false)
	public Integer getQuantidadePeriodo() {
		return quantidadePeriodo;
	}

	public void setQuantidadePeriodo(Integer quantidadePeriodo) {
		this.quantidadePeriodo = quantidadePeriodo;
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
		Curso other = (Curso) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Transient
	public Periodo[] getPeriodos() {
		Periodo[] periodos = Periodo.values();
		Periodo[] elementosArray = new Periodo[quantidadePeriodo];
		
		for (int i = 0; i < quantidadePeriodo; i++) {
			elementosArray[i] = periodos[i];
		}
		
		return elementosArray;
	}
}