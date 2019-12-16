package br.edu.ucv.estagio.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.edu.ucv.estagio.model.Curso;
import br.edu.ucv.estagio.repository.Cursos;
import br.edu.ucv.estagio.util.jpa.Transactional;

public class CadastroCursoService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Cursos cursos;
	
	@Transactional
	public Curso salvar(Curso curso) throws NegocioException {
		Curso cursoExiste = cursos.porNome(curso.getNome());
		
		if (cursoExiste != null && !cursoExiste.equals(curso)) {
			throw new NegocioException("O curso " + curso.getNome() + " já esta cadastrado.");
		}
		
		return cursos.guardar(curso);
	}
}