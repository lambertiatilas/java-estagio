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

import br.edu.ucv.estagio.model.Instituicao;
import br.edu.ucv.estagio.repository.filter.InstituicaoFilter;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jpa.Transactional;

public class Instituicoes implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public Instituicao guardar(Instituicao instituicao) {
		return manager.merge(instituicao);
	}
	
	@Transactional
	public void remover(Instituicao instituicao) throws NegocioException {
		try {
			instituicao = porId(instituicao.getId());
			manager.remove(instituicao);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Instituição não pode ser excluída.");
		}
	}
	
	public Instituicao porId(Long id) {
		return manager.find(Instituicao.class, id);
	}
	
	public List<Instituicao> instituicoes() {
		return manager.createQuery("from Instituicao", Instituicao.class).getResultList();
	}
	
	public Instituicao porNome(String nome) {
		try {
			return manager.createQuery("from Instituicao"
				+ " where upper(nome) = :nome"
				, Instituicao.class)
				.setParameter("nome", nome.toUpperCase())
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	private List<Predicate> criarPredicatesParaFiltro(InstituicaoFilter filtro, Root<Instituicao> instituicaoRoot) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();
		
		if (filtro.getId() != null) {
			predicates.add(builder.equal(instituicaoRoot.get("id"), filtro.getId()));
		}
		
		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(builder.like(builder.upper(instituicaoRoot.get("nome")), "%" + filtro.getNome().toUpperCase() + "%"));
		}
		
		return predicates;
	}
	
	public List<Instituicao> filtradas(InstituicaoFilter filtro) {
		From<?, ?> orderByFromEntity = null;
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Instituicao> criteriaQuery = builder.createQuery(Instituicao.class);
		Root<Instituicao> instituicaoRoot = criteriaQuery.from(Instituicao.class);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, instituicaoRoot);
		
		criteriaQuery.select(instituicaoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		
		if (filtro.getPropriedadeOrdenacao() != null) {
			String nomePropriedadeOrdenacao = filtro.getPropriedadeOrdenacao();
			orderByFromEntity = instituicaoRoot;
			
			if (filtro.getPropriedadeOrdenacao().contains(".")) {
				nomePropriedadeOrdenacao = nomePropriedadeOrdenacao.substring(filtro.getPropriedadeOrdenacao().indexOf(".") + 1);
			}
			
			if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.asc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			} else if (filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.desc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			}
		}
		
		TypedQuery<Instituicao> query = manager.createQuery(criteriaQuery);
		query.setFirstResult(filtro.getPrimeiroRegistro());
		query.setMaxResults(filtro.getQuantidadeRegistros());
		return query.getResultList();
	}
	
	public int quantidadeFiltrados(InstituicaoFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<Instituicao> instituicaoRoot = criteriaQuery.from(Instituicao.class);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, instituicaoRoot);
		
		criteriaQuery.select(builder.count(instituicaoRoot));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Long> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult().intValue();
	}
}