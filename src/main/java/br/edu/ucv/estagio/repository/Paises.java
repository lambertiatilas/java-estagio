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

import br.edu.ucv.estagio.model.Pais;
import br.edu.ucv.estagio.repository.filter.PaisFilter;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jpa.Transactional;

public class Paises implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public Pais guardar(Pais pais) {
		return manager.merge(pais);
	}
	
	@Transactional
	public void remover(Pais pais) throws NegocioException {
		try {
			pais = porId(pais.getId());
			manager.remove(pais);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("País não pode ser excluído.");
		}
	}
	
	public Pais porId(Long id) {
		return manager.find(Pais.class, id);
	}
	
	public Pais porNome(String nome) {
		try {
			return manager.createQuery("from Pais where upper(nome) = :nome", Pais.class)
				.setParameter("nome", nome.toUpperCase())
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public Pais porSigla(String sigla) {
		try {
			return manager.createQuery("from Pais where upper(sigla) = :sigla", Pais.class)
				.setParameter("sigla", sigla.toUpperCase())
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Pais> paises() {
		return manager.createQuery("from Pais", Pais.class).getResultList();
	}
	
	private List<Predicate> criarPredicatesParaFiltro(PaisFilter filtro, Root<Pais> paisRoot) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();
		
		if (filtro.getId() != null) {
			predicates.add(builder.equal(paisRoot.get("id"), filtro.getId()));
		}
		
		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(builder.like(builder.upper(paisRoot.get("nome")), "%" + filtro.getNome().toUpperCase() + "%"));
		}
		
		if (StringUtils.isNotBlank(filtro.getSigla())) {
			predicates.add(builder.equal(builder.upper(paisRoot.get("sigla")), filtro.getSigla().toUpperCase()));
		}
		
		return predicates;
	}
	
	public List<Pais> filtrados(PaisFilter filtro) {
		From<?, ?> orderByFromEntity = null;
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Pais> criteriaQuery = builder.createQuery(Pais.class);
		Root<Pais> paisRoot = criteriaQuery.from(Pais.class);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, paisRoot);
		
		criteriaQuery.select(paisRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		
		if (filtro.getPropriedadeOrdenacao() != null) {
			String nomePropriedadeOrdenacao = filtro.getPropriedadeOrdenacao();
			orderByFromEntity = paisRoot;
			
			if (filtro.getPropriedadeOrdenacao().contains(".")) {
				nomePropriedadeOrdenacao = nomePropriedadeOrdenacao.substring(filtro.getPropriedadeOrdenacao().indexOf(".") + 1);
			}
			
			if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.asc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			} else if (filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.desc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			}
		}
		
		TypedQuery<Pais> query = manager.createQuery(criteriaQuery);
		query.setFirstResult(filtro.getPrimeiroRegistro());
		query.setMaxResults(filtro.getQuantidadeRegistros());
		return query.getResultList();
	}
	
	public int quantidadeFiltrados(PaisFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<Pais> paisRoot = criteriaQuery.from(Pais.class);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, paisRoot);
		
		criteriaQuery.select(builder.count(paisRoot));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Long> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult().intValue();
	}
}