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
import br.edu.ucv.estagio.model.Municipio;
import br.edu.ucv.estagio.repository.filter.MunicipioFilter;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jpa.Transactional;

public class Municipios implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public Municipio guardar(Municipio municipio) {
		return manager.merge(municipio);
	}
	
	@Transactional
	public void remover(Municipio municipio) throws NegocioException {
		try {
			municipio = porId(municipio.getId());
			manager.remove(municipio);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Municipio não pode ser excluído.");
		}
	}
	
	public Municipio porId(Long id) {
		return manager.find(Municipio.class, id);
	}
	
	public Municipio porNome(Municipio municipio) {
		try {
			return manager.createQuery("from Municipio"
				+ " where upper(nome) = :nome"
				+ " and estado = :estado"
				, Municipio.class)
				.setParameter("nome", municipio.getNome().toUpperCase())
				.setParameter("estado", municipio.getEstado())
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Municipio> municipios() {
		return manager.createQuery("from Municipio", Municipio.class).getResultList();
	}
	
	public List<Municipio> municipiosDeEstado(Long estadoId) {
		return manager.createQuery("from Municipio where estado_id = :estado_id", Municipio.class)
			.setParameter("estado_id", estadoId)
			.getResultList();
	}
	
	private List<Predicate> criarPredicatesParaFiltro(MunicipioFilter filtro, Root<Municipio> municipioRoot, From<?, ?> estadoJoin) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();
		
		if (filtro.getId() != null) {
			predicates.add(builder.equal(municipioRoot.get("id"), filtro.getId()));
		}
		
		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(builder.like(builder.upper(municipioRoot.get("nome")), "%" + filtro.getNome().toUpperCase() + "%"));
		}
		
		if (StringUtils.isNotBlank(filtro.getEstado())) {
			predicates.add(builder.like(builder.upper(estadoJoin.get("nome")), "%" + filtro.getEstado().toUpperCase() + "%"));
		}
		
		return predicates;
	}
	
	public List<Municipio> filtrados(MunicipioFilter filtro) {
		From<?, ?> orderByFromEntity = null;
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Municipio> criteriaQuery = builder.createQuery(Municipio.class);
		Root<Municipio> municipioRoot = criteriaQuery.from(Municipio.class);
		From<?, ?> estadoJoin = (From<?, ?>) municipioRoot.fetch("estado", JoinType.INNER);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, municipioRoot, estadoJoin);
		
		criteriaQuery.select(municipioRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		
		if (filtro.getPropriedadeOrdenacao() != null) {
			String nomePropriedadeOrdenacao = filtro.getPropriedadeOrdenacao();
			orderByFromEntity = municipioRoot;
			
			if (filtro.getPropriedadeOrdenacao().contains(".")) {
				nomePropriedadeOrdenacao = nomePropriedadeOrdenacao.substring(filtro.getPropriedadeOrdenacao().indexOf(".") + 1);
			}
			
			if (filtro.getPropriedadeOrdenacao().startsWith("estado.")) {
				orderByFromEntity = estadoJoin;
			}
			
			if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.asc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			} else if (filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.desc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			}
		}
		
		TypedQuery<Municipio> query = manager.createQuery(criteriaQuery);
		query.setFirstResult(filtro.getPrimeiroRegistro());
		query.setMaxResults(filtro.getQuantidadeRegistros());
		return query.getResultList();
	}
	
	public int quantidadeFiltrados(MunicipioFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<Municipio> municipioRoot = criteriaQuery.from(Municipio.class);
		Join<Municipio, Estado> estadoJoin = municipioRoot.join("estado", JoinType.INNER);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, municipioRoot, estadoJoin);
		
		criteriaQuery.select(builder.count(municipioRoot));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Long> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult().intValue();
	}
}