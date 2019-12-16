package br.edu.ucv.estagio.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import br.edu.ucv.estagio.util.string.StringUtil;

@Entity
@Table(name = "clausula")
public class Clausula implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private TipoClausula tipo;
	private Integer numeroClausula;
	private String titulo;
	private String descricao;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotNull
	@Enumerated
	@Column(nullable = false)
	public TipoClausula getTipo() {
		return tipo;
	}

	public void setTipo(TipoClausula tipo) {
		this.tipo = tipo;
	}

	@NotNull
	@Min(value = 1)
	@Max(value = 20)
	@Column(name = "numero_clausula", nullable = false)
	public Integer getNumeroClausula() {
		return numeroClausula;
	}

	public void setNumeroClausula(Integer numeroClausula) {
		this.numeroClausula = numeroClausula;
	}

	@NotBlank
	@Size(max = 80)
	@Column(length = 80, nullable = false)
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = StringUtil.trimAll(titulo);
	}

	@NotBlank
	@Column(columnDefinition = "text", nullable = false)
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao.trim();
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
		Clausula other = (Clausula) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}