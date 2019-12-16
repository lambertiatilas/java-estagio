package br.edu.ucv.estagio.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import br.edu.ucv.estagio.model.Curso;
import br.edu.ucv.estagio.repository.filter.CursoFilter;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jpa.Transactional;

public class Cursos implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public Curso guardar(Curso curso) {
		return manager.merge(curso);
	}
	
	@Transactional
	public void remover(Curso curso) throws NegocioException {
		try {
			curso = porId(curso.getId());
			manager.remove(curso);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Curso não pode ser excluído.");
		}
	}
	
	public Curso porId(Long id) {
		return manager.find(Curso.class, id);
	}
	
	public List<Curso> cursos() {
		return manager.createQuery("from Curso", Curso.class).getResultList();
	}
	
	public Curso porNome(String nome) {
		try {
			return manager.createQuery("from Curso where upper(nome) = :nome", Curso.class)
				.setParameter("nome", nome.toUpperCase())
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	private List<Predicate> criarPredicatesParaFiltro(CursoFilter filtro, Root<Curso> cursoRoot) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();
		
		if (filtro.getId() != null) {
			predicates.add(builder.equal(cursoRoot.get("id"), filtro.getId()));
		}
		
		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(builder.like(builder.upper(cursoRoot.get("nome")), "%" + filtro.getNome().toUpperCase() + "%"));
		}
		
		if (filtro.getGrau() != null) {
			predicates.add(cursoRoot.get("grau").in(filtro.getGrau()));
		}
		
		return predicates;
	}
	
	public List<Curso> filtrados(CursoFilter filtro) {
		From<?, ?> orderByFromEntity = null;
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Curso> criteriaQuery = builder.createQuery(Curso.class);
		Root<Curso> cursoRoot = criteriaQuery.from(Curso.class);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, cursoRoot);
		
		criteriaQuery.select(cursoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		
		if (filtro.getPropriedadeOrdenacao() != null) {
			String nomePropriedadeOrdenacao = filtro.getPropriedadeOrdenacao();
			orderByFromEntity = cursoRoot;
			
			if (filtro.getPropriedadeOrdenacao().contains(".")) {
				nomePropriedadeOrdenacao = nomePropriedadeOrdenacao.substring(filtro.getPropriedadeOrdenacao().indexOf(".") + 1);
			}
			
			if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.asc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			} else if (filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.desc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			}
		}
		
		TypedQuery<Curso> query = manager.createQuery(criteriaQuery);
		query.setFirstResult(filtro.getPrimeiroRegistro());
		query.setMaxResults(filtro.getQuantidadeRegistros());
		return query.getResultList();
	}
	
	public int quantidadeFiltrados(CursoFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<Curso> cursoRoot = criteriaQuery.from(Curso.class);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, cursoRoot);
		
		criteriaQuery.select(builder.count(cursoRoot));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Long> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult().intValue();
	}
}