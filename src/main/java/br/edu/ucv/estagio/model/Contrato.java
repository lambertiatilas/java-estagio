package br.edu.ucv.estagio.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import br.edu.ucv.estagio.util.string.StringUtil;

@Entity
@Table(name = "contrato")
public class Contrato implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Date horario;
	private DiaSemana diaSemana;
	private Integer cargaHoraria;
	private String setor;
	private Date dataInicio;
	private Date dataFim;
	private Supervisor supervisor;
	private String atividades;
	private Estagio estagio;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotNull
	@Temporal(TemporalType.TIME)
	@Column(nullable = false)
	public Date getHorario() {
		return horario;
	}

	public void setHorario(Date horario) {
		this.horario = horario;
	}

	@NotNull
	@Enumerated
	@Column(name = "dia_semana", nullable = false)
	public DiaSemana getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(DiaSemana diaSemana) {
		this.diaSemana = diaSemana;
	}

	@NotNull
	@Min(value = 20)
	@Max(value = 40)
	@Column(name = "carga_horaria", nullable = false)
	public Integer getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(Integer cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	@NotBlank
	@Size(max = 80)
	@Column(length = 80, nullable = false)
	public String getSetor() {
		return setor;
	}

	public void setSetor(String setor) {
		this.setor = StringUtil.trimAll(setor);
	}

	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name = "data_inicio", nullable = false)
	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "data_fim")
	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	@NotNull
	@OneToOne(cascade = CascadeType.ALL)
	public Supervisor getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(Supervisor supervisor) {
		this.supervisor = supervisor;
	}

	@NotBlank
	@Column(columnDefinition = "text", nullable = false)
	public String getAtividades() {
		return atividades;
	}

	public void setAtividades(String atividades) {
		this.atividades = atividades.trim();
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
		Contrato other = (Contrato) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}