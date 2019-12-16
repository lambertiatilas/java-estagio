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

import br.edu.ucv.estagio.model.Bairro;
import br.edu.ucv.estagio.model.Municipio;
import br.edu.ucv.estagio.repository.filter.BairroFilter;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jpa.Transactional;

public class Bairros implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public Bairro guardar(Bairro bairro) {
		return manager.merge(bairro);
	}
	
	@Transactional
	public void remover(Bairro bairro) throws NegocioException {
		try {
			bairro = porId(bairro.getId());
			manager.remove(bairro);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Bairro não pode ser excluído.");
		}
	}
	
	public Bairro porId(Long id) {
		return manager.find(Bairro.class, id);
	}
	
	public List<Bairro> bairros() {
		return manager.createQuery("from Bairro", Bairro.class).getResultList();
	}
	
	public Bairro porNome(Bairro bairro) {
		try {
			return manager.createQuery("from Bairro"
				+ " where upper(nome) = :nome"
				+ " and municipio = :municipio"
				, Bairro.class)
				.setParameter("nome", bairro.getNome().toUpperCase())
				.setParameter("municipio", bairro.getMunicipio())
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Bairro> bairrosDeMunicipio(Long municipioId) {
		return manager.createQuery("from Bairro where municipio_id = :municipio_id", Bairro.class)
			.setParameter("municipio_id", municipioId)
			.getResultList();
	}
	
	private List<Predicate> criarPredicatesParaFiltro(BairroFilter filtro, Root<Bairro> bairroRoot, From<?, ?> municipioJoin) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();
		
		if (filtro.getId() != null) {
			predicates.add(builder.equal(bairroRoot.get("id"), filtro.getId()));
		}
		
		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(builder.like(builder.upper(bairroRoot.get("nome")), "%" + filtro.getNome().toUpperCase() + "%"));
		}
		
		if (StringUtils.isNotBlank(filtro.getMunicipio())) {
			predicates.add(builder.like(builder.upper(municipioJoin.get("nome")), "%" + filtro.getMunicipio().toUpperCase() + "%"));
		}
		
		return predicates;
	}
	
	public List<Bairro> filtrados(BairroFilter filtro) {
		From<?, ?> orderByFromEntity = null;
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Bairro> criteriaQuery = builder.createQuery(Bairro.class);
		Root<Bairro> bairroRoot = criteriaQuery.from(Bairro.class);
		From<?, ?> municipioJoin = (From<?, ?>) bairroRoot.fetch("municipio", JoinType.INNER);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, bairroRoot, municipioJoin);
		
		criteriaQuery.select(bairroRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		
		if (filtro.getPropriedadeOrdenacao() != null) {
			String nomePropriedadeOrdenacao = filtro.getPropriedadeOrdenacao();
			orderByFromEntity = bairroRoot;
			
			if (filtro.getPropriedadeOrdenacao().contains(".")) {
				nomePropriedadeOrdenacao = nomePropriedadeOrdenacao.substring(filtro.getPropriedadeOrdenacao().indexOf(".") + 1);
			}
			
			if (filtro.getPropriedadeOrdenacao().startsWith("municipio.")) {
				orderByFromEntity = municipioJoin;
			}
			
			if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.asc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			} else if (filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.desc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			}
		}
		
		TypedQuery<Bairro> query = manager.createQuery(criteriaQuery);
		query.setFirstResult(filtro.getPrimeiroRegistro());
		query.setMaxResults(filtro.getQuantidadeRegistros());
		return query.getResultList();
	}
	
	public int quantidadeFiltrados(BairroFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<Bairro> bairroRoot = criteriaQuery.from(Bairro.class);
		Join<Bairro, Municipio> municipioJoin = bairroRoot.join("municipio", JoinType.INNER);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, bairroRoot, municipioJoin);
		
		criteriaQuery.select(builder.count(bairroRoot));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Long> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult().intValue();
	}
}