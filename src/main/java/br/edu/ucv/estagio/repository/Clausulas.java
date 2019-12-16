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

import br.edu.ucv.estagio.model.Clausula;
import br.edu.ucv.estagio.repository.filter.ClausulaFilter;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jpa.Transactional;

public class Clausulas implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public Clausula guardar(Clausula clausula) {
		return manager.merge(clausula);
	}
	
	@Transactional
	public void remover(Clausula clausula) throws NegocioException {
		try {
			clausula = porId(clausula.getId());
			manager.remove(clausula);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Cláusula não pode ser excluída.");
		}
	}
	
	public Clausula porId(Long id) {
		return manager.find(Clausula.class, id);
	}
	
	public List<Clausula> clausulas() {
		return manager.createQuery("from Clausula", Clausula.class).getResultList();
	}
	
	public Clausula existe(Clausula clausula) {
		try {
			return manager.createQuery("from Clausula"
				+ " where tipo = :tipo"
				+ " and numeroClausula = :numeroClausula"
			, Clausula.class)
			.setParameter("tipo", clausula.getTipo())
			.setParameter("numeroClausula", clausula.getNumeroClausula())
			.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	private List<Predicate> criarPredicatesParaFiltro(ClausulaFilter filtro, Root<Clausula> clausulaRoot) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();
		
		if (filtro.getId() != null) {
			predicates.add(builder.equal(clausulaRoot.get("id"), filtro.getId()));
		}
		
		if (filtro.getTipo() != null) {
			predicates.add(clausulaRoot.get("tipo").in(filtro.getTipo()));
		}
		
		if (filtro.getNumeroClausula() != null) {
			predicates.add(builder.equal(clausulaRoot.get("numeroClausula"), filtro.getNumeroClausula()));
		}
		
		if (StringUtils.isNotBlank(filtro.getTitulo())) {
			predicates.add(builder.like(builder.upper(clausulaRoot.get("titulo")), "%" + filtro.getTitulo().toUpperCase() + "%"));
		}
		
		return predicates;
	}
	
	public List<Clausula> filtradas(ClausulaFilter filtro) {
		From<?, ?> orderByFromEntity = null;
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Clausula> criteriaQuery = builder.createQuery(Clausula.class);
		Root<Clausula> clausulaRoot = criteriaQuery.from(Clausula.class);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, clausulaRoot);
		
		criteriaQuery.select(clausulaRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		
		if (filtro.getPropriedadeOrdenacao() != null) {
			String nomePropriedadeOrdenacao = filtro.getPropriedadeOrdenacao();
			orderByFromEntity = clausulaRoot;
			
			if (filtro.getPropriedadeOrdenacao().contains(".")) {
				nomePropriedadeOrdenacao = nomePropriedadeOrdenacao.substring(filtro.getPropriedadeOrdenacao().indexOf(".") + 1);
			}
			
			if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.asc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			} else if (filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.desc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			}
		}
		
		TypedQuery<Clausula> query = manager.createQuery(criteriaQuery);
		query.setFirstResult(filtro.getPrimeiroRegistro());
		query.setMaxResults(filtro.getQuantidadeRegistros());
		return query.getResultList();
	}
	
	public int quantidadeFiltrados(ClausulaFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<Clausula> clausulaRoot = criteriaQuery.from(Clausula.class);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, clausulaRoot);
		
		criteriaQuery.select(builder.count(clausulaRoot));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Long> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult().intValue();
	}
}