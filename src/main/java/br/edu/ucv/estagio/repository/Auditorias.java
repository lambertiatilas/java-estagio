package br.edu.ucv.estagio.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import br.edu.ucv.estagio.model.Auditoria;
import br.edu.ucv.estagio.model.Pessoa;
import br.edu.ucv.estagio.model.TabelaAuditoria;
import br.edu.ucv.estagio.repository.filter.AuditoriaFilter;

public class Auditorias implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public void guardar(Auditoria auditoria) {
		manager.merge(auditoria);
	}
	
	public Auditoria existe(TabelaAuditoria tabelaNome, Long tabelaId) {
		try {
			return manager.createQuery("from Auditoria"
				+ " where tabelaNome = :tabelaNome"
				+ " and tabelaId = :tabelaId"
				, Auditoria.class)
			.setParameter("tabelaNome", tabelaNome)
			.setParameter("tabelaId", tabelaId)
			.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	private List<Predicate> criarPredicatesParaFiltro(AuditoriaFilter filtro, Root<Auditoria> auditoriaRoot, From<?, ?> criadoPorJoin, From<?, ?> modificadoPorJoin) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();
		
		if (filtro.getId() != null) {
			predicates.add(builder.equal(auditoriaRoot.get("id"), filtro.getId()));
		}
		
		if (filtro.getTabelaNome() != null) {
			predicates.add(auditoriaRoot.get("tabelaNome").in(filtro.getTabelaNome()));
		}
		
		if (filtro.getTabelaId() != null) {
			predicates.add(builder.equal(auditoriaRoot.get("tabelaId"), filtro.getTabelaId()));
		}
		
		if (filtro.getAcao() != null) {
			predicates.add(auditoriaRoot.get("acao").in(filtro.getAcao()));
		}
		
		if (filtro.getDataCriacaoDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(auditoriaRoot.get("dataCriacao"), filtro.getDataCriacaoDe()));
		}
		
		if (filtro.getDataCriacaoAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(auditoriaRoot.get("dataCriacao"), filtro.getDataCriacaoAte()));
		}
		
		if (StringUtils.isNotBlank(filtro.getCriadoPor())) {
			predicates.add(builder.like(builder.upper(criadoPorJoin.get("nome")), "%" + filtro.getCriadoPor().toUpperCase() + "%"));
		}
		
		if (filtro.getDataModificacaoDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(auditoriaRoot.get("dataModificacao"), filtro.getDataModificacaoDe()));
		}
		
		if (filtro.getDataModificacaoAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(auditoriaRoot.get("dataModificacao"), filtro.getDataModificacaoAte()));
		}
		
		if (StringUtils.isNotBlank(filtro.getModificadoPor())) {
			predicates.add(builder.like(builder.upper(modificadoPorJoin.get("nome")), "%" + filtro.getModificadoPor().toUpperCase() + "%"));
		}
		
		return predicates;
	}
	
	public List<Auditoria> filtrados(AuditoriaFilter filtro) {
		From<?, ?> orderByFromEntity = null;
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Auditoria> criteriaQuery = builder.createQuery(Auditoria.class);
		Root<Auditoria> auditoriaRoot = criteriaQuery.from(Auditoria.class);
		From<?, ?> criadoPorJoin = (From<?, ?>) auditoriaRoot.fetch("criadoPor", JoinType.INNER);
		From<?, ?> modificadoPorJoin = (From<?, ?>) auditoriaRoot.fetch("modificadoPor", JoinType.INNER);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, auditoriaRoot, criadoPorJoin, modificadoPorJoin);
		
		criteriaQuery.select(auditoriaRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		
		if (filtro.getPropriedadeOrdenacao() != null) {
			String nomePropriedadeOrdenacao = filtro.getPropriedadeOrdenacao();
			orderByFromEntity = auditoriaRoot;
			
			if (filtro.getPropriedadeOrdenacao().contains(".")) {
				nomePropriedadeOrdenacao = nomePropriedadeOrdenacao.substring(filtro.getPropriedadeOrdenacao().indexOf(".") + 1);
			}
			
			if (filtro.getPropriedadeOrdenacao().startsWith("criadoPor.")) {
				orderByFromEntity = criadoPorJoin;
			}
			
			if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.asc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			} else if (filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.desc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			}
		}
		
		TypedQuery<Auditoria> query = manager.createQuery(criteriaQuery);
		query.setFirstResult(filtro.getPrimeiroRegistro());
		query.setMaxResults(filtro.getQuantidadeRegistros());
		return query.getResultList();
	}
	
	public int quantidadeFiltrados(AuditoriaFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<Auditoria> auditoriaRoot = criteriaQuery.from(Auditoria.class);
		Join<Auditoria, Pessoa> criadoPorjoin = auditoriaRoot.join("criadoPor", JoinType.INNER);
		Join<Auditoria, Pessoa> modificadoPorJoin = auditoriaRoot.join("modificadoPor", JoinType.INNER);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, auditoriaRoot, criadoPorjoin, modificadoPorJoin);
		
		criteriaQuery.select(builder.count(auditoriaRoot));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Long> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult().intValue();
	}
}