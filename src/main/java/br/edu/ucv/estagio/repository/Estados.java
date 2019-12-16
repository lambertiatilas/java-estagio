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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import br.edu.ucv.estagio.model.Estado;
import br.edu.ucv.estagio.model.Pais;
import br.edu.ucv.estagio.repository.filter.EstadoFilter;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jpa.Transactional;

public class Estados implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public Estado guardar(Estado estado) {
		return manager.merge(estado);
	}
	
	@Transactional
	public void remover(Estado estado) throws NegocioException {
		try {
			estado = porId(estado.getId());
			manager.remove(estado);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Estado não pode ser excluído.");
		}
	}
	
	public Estado porId(Long id) {
		return manager.find(Estado.class, id);
	}
	
	public Estado porNome(Estado estado) {
		try {
			return manager.createQuery("from Estado"
				+ " where upper(nome) = :nome"
				+ " and pais = :pais"
			, Estado.class)
			.setParameter("nome", estado.getNome().toUpperCase())
			.setParameter("pais", estado.getPais())
			.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public Estado porUf(Estado estado) {
		try {
			return manager.createQuery("from Estado"
				+ " where upper(uf) = :uf"
				+ " and pais = :pais"
			, Estado.class)
			.setParameter("uf", estado.getUf().toUpperCase())
			.setParameter("pais", estado.getPais())
			.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Estado> estados() {
		return manager.createQuery("from Estado", Estado.class).getResultList();
	}
	
	public List<Estado> estadosDePais(Long paisId) {
		return manager.createQuery("from Estado where pais_id = :pais_id", Estado.class)
			.setParameter("pais_id", paisId)
			.getResultList();
	}
	
	private List<Predicate> criarPredicatesParaFiltro(EstadoFilter filtro, Root<Estado> estadoRoot, From<?, ?> paisJoin) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();
		
		if (filtro.getId() != null) {
			predicates.add(builder.equal(estadoRoot.get("id"), filtro.getId()));
		}
		
		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(builder.like(builder.upper(estadoRoot.get("nome")), "%" + filtro.getNome().toUpperCase() + "%"));
		}
		
		if (StringUtils.isNotBlank(filtro.getUf())) {
			predicates.add(builder.equal(builder.upper(estadoRoot.get("uf")), filtro.getUf().toUpperCase()));
		}
		
		if (StringUtils.isNotBlank(filtro.getPais())) {
			predicates.add(builder.like(builder.upper(paisJoin.get("nome")), "%" + filtro.getPais().toUpperCase() + "%"));
		}
		
		return predicates;
	}
	
	public List<Estado> filtrados(EstadoFilter filtro) {
		From<?, ?> orderByFromEntity = null;
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Estado> criteriaQuery = builder.createQuery(Estado.class);
		Root<Estado> estadoRoot = criteriaQuery.from(Estado.class);
		From<?, ?> paisJoin = (From<?, ?>) estadoRoot.fetch("pais", JoinType.INNER);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, estadoRoot, paisJoin);
		
		criteriaQuery.select(estadoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		
		if (filtro.getPropriedadeOrdenacao() != null) {
			String nomePropriedadeOrdenacao = filtro.getPropriedadeOrdenacao();
			orderByFromEntity = estadoRoot;
			
			if (filtro.getPropriedadeOrdenacao().contains(".")) {
				nomePropriedadeOrdenacao = nomePropriedadeOrdenacao.substring(filtro.getPropriedadeOrdenacao().indexOf(".") + 1);
			}
			
			if (filtro.getPropriedadeOrdenacao().startsWith("pais.")) {
				orderByFromEntity = paisJoin;
			}
			
			if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.asc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			} else if (filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.desc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			}
		}
		
		TypedQuery<Estado> query = manager.createQuery(criteriaQuery);
		query.setFirstResult(filtro.getPrimeiroRegistro());
		query.setMaxResults(filtro.getQuantidadeRegistros());
		return query.getResultList();
	}
	
	public int quantidadeFiltrados(EstadoFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<Estado> estadoRoot = criteriaQuery.from(Estado.class);
		Join<Estado, Pais> paisJoin = estadoRoot.join("pais", JoinType.INNER);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, estadoRoot, paisJoin);
		
		criteriaQuery.select(builder.count(estadoRoot));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Long> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult().intValue();
	}
}