package br.edu.ucv.estagio.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.edu.ucv.estagio.model.Bairro;
import br.edu.ucv.estagio.model.Endereco;
import br.edu.ucv.estagio.model.Estado;
import br.edu.ucv.estagio.model.Municipio;
import br.edu.ucv.estagio.model.Pais;
import br.edu.ucv.estagio.repository.Bairros;
import br.edu.ucv.estagio.repository.Estados;
import br.edu.ucv.estagio.repository.Municipios;
import br.edu.ucv.estagio.repository.Paises;

public abstract class CadastroComEnderecoBean {

	@Inject
	private Paises paises;

	@Inject
	private Estados estados;

	@Inject
	private Municipios municipios;

	@Inject
	private Bairros bairros;

	protected List<Pais> listaPaises = new ArrayList<>();
	protected List<Estado> listaEstados = new ArrayList<>();
	protected List<Municipio> listaMunicipios = new ArrayList<>();
	protected List<Bairro> listaBairros = new ArrayList<>();

	protected Pais novoPais() {
		Pais pais = new Pais();
		return pais;
	}

	protected Estado novoEstado() {
		Estado estado = new Estado();
		estado.setPais(novoPais());
		return estado;
	}

	protected Municipio novoMunicipio() {
		Municipio municipio = new Municipio();
		municipio.setEstado(novoEstado());
		return municipio;
	}

	protected Bairro novoBairro() {
		Bairro bairro = new Bairro();
		bairro.setMunicipio(novoMunicipio());
		return bairro;
	}

	protected Endereco novoEndereco() {
		Endereco endereco = new Endereco();
		endereco.setBairro(novoBairro());
		return endereco;
	}

	public List<Pais> getListaPaises() {
		return listaPaises;
	}

	public List<Estado> getListaEstados() {
		return listaEstados;
	}

	public List<Municipio> getListaMunicipios() {
		return listaMunicipios;
	}

	public List<Bairro> getListaBairros() {
		return listaBairros;
	}

	public void iniciarEstados() {
		carregarPaises();
	}

	public void iniciarMunicipios(Municipio municipio) {
		carregarPaises();
		carregarEstados(municipio.getEstado().getPais());
	}

	public void iniciarBairros(Bairro bairro) {
		carregarPaises();
		carregarEstados(bairro.getMunicipio().getEstado().getPais());
		carregarMunicipios(bairro.getMunicipio().getEstado());
	}

	public void iniciarEnderecos(Endereco endereco) {
		carregarPaises();
		carregarEstados(endereco.getBairro().getMunicipio().getEstado().getPais());
		carregarMunicipios(endereco.getBairro().getMunicipio().getEstado());
		carregarBairros(endereco.getBairro().getMunicipio());
	}

	public void carregarPaises() {
		listaPaises = paises.paises();
	}

	public void carregarEstados(Pais pais) {
		if (pais.getId() != null) {
			listaEstados = estados.estadosDePais(pais.getId());
		}
	}

	public void carregarMunicipios(Estado estado) {
		if (estado.getId() != null) {
			listaMunicipios = municipios.municipiosDeEstado(estado.getId());
		}
	}

	public void carregarBairros(Municipio municipio) {
		if (municipio.getId() != null) {
			listaBairros = bairros.bairrosDeMunicipio(municipio.getId());
		}
	}
}