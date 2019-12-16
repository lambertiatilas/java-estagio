package br.edu.ucv.estagio.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.crypto.bcrypt.BCrypt;

import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.string.StringUtil;

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String senha;
	private Status status = Status.ATIVO;
	private List<Grupo> grupos = new ArrayList<>();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotBlank
	@Size(max = 60)
	@Column(columnDefinition = "CHAR(60)", nullable = false)
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Enumerated
	@Column(nullable = false)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST })
	@JoinTable(name = "usuario_grupo", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "grupo_id"))
	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}

	@Transient
	public boolean isAtivo() {
		return Status.ATIVO.equals(status);
	}

	@Transient
	public boolean isInativo() {
		return Status.INATIVO.equals(status);
	}

	public void conferirSenhas(String senhaAtual, String senhaNova) throws NegocioException {
		if (BCrypt.checkpw(senhaAtual, senha)) {
			senha = StringUtil.encoder(senhaNova);
		} else {
			throw new NegocioException("A senha atual está incorreta.");
		}
	}
}