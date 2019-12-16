package br.edu.ucv.estagio.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "acompanhamento")
public class Acompanhamento implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String atividades;
	private String autoAvaliacao;
	private String dificuldades;
	private String melhorias;
	private String avaliacaoSupervisor;
	private String avaliacaoProfessor;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotBlank
	@Column(columnDefinition = "text", nullable = false)
	public String getAtividades() {
		return atividades;
	}

	public void setAtividades(String atividades) {
		this.atividades = atividades.trim();
	}

	@NotBlank
	@Column(name = "auto_avaliacao", columnDefinition = "text", nullable = false)
	public String getAutoAvaliacao() {
		return autoAvaliacao;
	}

	public void setAutoAvaliacao(String autoAvaliacao) {
		this.autoAvaliacao = autoAvaliacao.trim();
	}

	@NotBlank
	@Column(columnDefinition = "text", nullable = false)
	public String getDificuldades() {
		return dificuldades;
	}

	public void setDificuldades(String dificuldades) {
		this.dificuldades = dificuldades.trim();
	}

	@NotBlank
	@Column(columnDefinition = "text", nullable = false)
	public String getMelhorias() {
		return melhorias;
	}

	public void setMelhorias(String melhorias) {
		this.melhorias = melhorias.trim();
	}

	@NotBlank
	@Column(name = "avaliacao_supervisor", columnDefinition = "text", nullable = false)
	public String getAvaliacaoSupervisor() {
		return avaliacaoSupervisor;
	}

	public void setAvaliacaoSupervisor(String avaliacaoSupervisor) {
		this.avaliacaoSupervisor = avaliacaoSupervisor.trim();
	}

	@NotBlank
	@Column(name = "avaliacao_professor", columnDefinition = "text", nullable = false)
	public String getAvaliacaoProfessor() {
		return avaliacaoProfessor;
	}

	public void setAvaliacaoProfessor(String avaliacaoProfessor) {
		this.avaliacaoProfessor = avaliacaoProfessor.trim();
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
		Acompanhamento other = (Acompanhamento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}