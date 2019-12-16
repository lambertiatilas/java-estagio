package br.edu.ucv.estagio.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import br.edu.ucv.estagio.model.Pessoa;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jpa.Transactional;

public abstract class Pessoas implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	protected EntityManager manager;
	
	public Pessoa guardar(Pessoa pessoa) {
		return manager.merge(pessoa);
	}
	
	@Transactional
	public void remover(Pessoa pessoa) throws NegocioException {
		try {
			pessoa = porId(pessoa.getId());
			manager.remove(pessoa);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Pessoa não pode ser excluída.");
		}
	}
	
	public Pessoa porId(Long id) {
		return manager.find(Pessoa.class, id);
	}
		
	public Pessoa porCpf(String cpf) {
		try {
			return manager.createQuery("from Pessoa where cpf = :cpf", Pessoa.class)
				.setParameter("cpf", cpf)
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public Pessoa porEmail(String email) {
		try {
			return manager.createQuery("select pessoa from Pessoa pessoa inner join pessoa.contato contato"
					+ " where lower(contato.email) = :email"
					, Pessoa.class)
				.setParameter("email", email.toLowerCase())
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Pessoa> pessoas() {
		return manager.createQuery("from Pessoa", Pessoa.class).getResultList();
	}
}