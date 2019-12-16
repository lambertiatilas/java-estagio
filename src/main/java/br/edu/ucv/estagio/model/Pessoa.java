package br.edu.ucv.estagio.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;
import org.primefaces.event.FileUploadEvent;

import br.edu.ucv.estagio.util.image.ImageResizer;
import br.edu.ucv.estagio.util.string.StringUtil;

@Entity
@Table(name = "pessoa")
public class Pessoa implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private TipoPessoa tipo;
	private String nome;
	private String cpf;
	private Endereco endereco;
	private Contato contato;
	private Usuario usuario;
	private byte[] foto;
	private List<Estagio> estagios = new ArrayList<>();

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
	@Column(name = "tipo", nullable = false)
	public TipoPessoa getTipo() {
		return tipo;
	}

	public void setTipo(TipoPessoa tipo) {
		this.tipo = tipo;
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
	@CPF(message = "inválido!")
	@Column(columnDefinition = "CHAR(14)", nullable = false, unique = true)
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	@OneToOne(cascade = CascadeType.ALL)
	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
	}

	@OneToOne(cascade = CascadeType.ALL)
	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	@OneToOne(cascade = CascadeType.ALL)
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	@OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	public List<Estagio> getEstagios() {
		return estagios;
	}

	public void setEstagios(List<Estagio> estagios) {
		this.estagios = estagios;
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
		Pessoa other = (Pessoa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Transient
	public boolean isNova() {
		return id == null;
	}

	@Transient
	public boolean isExistente() {
		return !isNova();
	}

	@Transient
	public boolean isUsuarioExistente() {
		return isExistente() && usuario != null;
	}

	@Transient
	public boolean isUsuarioNaoExistente() {
		return !isUsuarioExistente();
	}

	public String gerarSenhaUsuario() {
		return StringUtil
				.encoder(cpf.replaceAll("[^0-9]", "") + String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
	}

	public void adicionarFoto(FileUploadEvent event) {
		foto = ImageResizer.resize(event.getFile().getContents(), 145, 145);
	}

	public void removerFoto() {
		foto = null;
	}

	public void adicionarEstagioVazio(Endereco endereco) {
		EmpresaEstagio empresaEstagio = new EmpresaEstagio();
		empresaEstagio.setEndereco(endereco);
		empresaEstagio.setContato(new Contato());
		
		Estagio estagio = new Estagio();
		estagio.setEmpresaEstagio(empresaEstagio);
		estagio.setPessoaEstagio(new PessoaEstagio());
		estagio.setPessoa(this);
		estagio.setStatus(StatusEstagio.PENDENTE);
		estagio.adcionarNovoContrato();
		estagios.add(0, estagio);
	}

	@Transient
	public List<Estagio> getEstagiosNaoFinalizados() {
		List<Estagio> estagios = new ArrayList<>();
		
		for (Estagio estagio : this.estagios) {
			if (estagio.isNaoFinalizado()) {
				estagios.add(estagio);
			}
		}
		
		return estagios;
	}
	
	public void removerItemVazio() {
		Estagio primeiroItem = estagios.get(0);
		
		if (primeiroItem != null && primeiroItem.getPessoaEstagio().getMatricula() == null) {
			estagios.remove(primeiroItem);
		}
	}
	
	public Integer cargaHorariaEstagiosNaoFinalizados() {
		Integer cargaHoraria = 0;
		
		for (Estagio estagio: estagios) {
	    	if (estagio.isAlteravel()) {
	    		cargaHoraria += estagio.getContratos().get(estagio.getContratos().size() - 1).getCargaHoraria();
	    	}
	    }
	 
		return cargaHoraria;
	}
}