package br.edu.ucv.estagio.util.endereco;

import br.edu.ucv.estagio.model.Bairro;
import br.edu.ucv.estagio.model.Endereco;
import br.edu.ucv.estagio.model.Estado;
import br.edu.ucv.estagio.model.Municipio;
import br.edu.ucv.estagio.model.Pais;

public class InicializarEndereco {

	private static Pais pais;
	private static Estado estado;
	private static Municipio municipio;
	private static Bairro bairro;
	private static Endereco endereco;

	public static Pais getPais() {
		pais = new Pais();
		return pais;
	}

	public static Estado getEstado() {
		estado = new Estado();
		estado.setPais(getPais());
		return estado;
	}

	public static Municipio getMunicipio() {
		municipio = new Municipio();
		municipio.setEstado(getEstado());
		return municipio;
	}

	public static Bairro getBairro() {
		bairro = new Bairro();
		bairro.setMunicipio(getMunicipio());
		return bairro;
	}

	public static Endereco getEndereco() {
		endereco = new Endereco();
		endereco.setBairro(getBairro());
		return endereco;
	}
}