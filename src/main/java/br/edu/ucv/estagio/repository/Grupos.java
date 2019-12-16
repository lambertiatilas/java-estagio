package br.edu.ucv.estagio.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.edu.ucv.estagio.model.Grupo;

public class Grupos implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;
	
	public Grupo porId(Long id) {
		return manager.find(Grupo.class, id);
	}
	
	public Grupo porNome(String nome) {
		try {
			return manager.createQuery("from Grupo where upper(nome) = :nome", Grupo.class)
				.setParameter("nome", nome.toUpperCase())
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Grupo> grupos() {
		return this.manager.createQuery("from Grupo", Grupo.class).getResultList();
	}
}