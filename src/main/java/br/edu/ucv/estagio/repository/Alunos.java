package br.edu.ucv.estagio.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import br.edu.ucv.estagio.model.Pessoa;
import br.edu.ucv.estagio.model.TipoPessoa;
import br.edu.ucv.estagio.repository.filter.PessoaFilter;

public class Alunos extends Pessoas implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public Pessoa porIdAluno(Long id) {
		try {
			return manager.createQuery("from Pessoa"
				+ " where id = :id"
				+ " and tipo = :tipo"
				, Pessoa.class)
			.setParameter("id", id)
			.setParameter("tipo", TipoPessoa.ALUNO)
			.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	private List<Predicate> criarPredicatesParaFiltro(PessoaFilter filtro, Root<Pessoa> pessoaRoot) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();
		
		predicates.add(pessoaRoot.get("tipo").in(TipoPessoa.ALUNO));
		
		if (filtro.getId() != null) {
			predicates.add(builder.equal(pessoaRoot.get("id"), filtro.getId()));
		}
		
		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(builder.like(builder.upper(pessoaRoot.get("nome")), "%" + filtro.getNome().toUpperCase() + "%"));
		}
		
		if (StringUtils.isNotBlank(filtro.getCpf())) {
			predicates.add(builder.equal(pessoaRoot.get("cpf"), filtro.getCpf()));
		}
		
		return predicates;
	}
	
	public List<Pessoa> filtrados(PessoaFilter filtro) {
		From<?, ?> orderByFromEntity = null;
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Pessoa> criteriaQuery = builder.createQuery(Pessoa.class);
		Root<Pessoa> pessoaRoot = criteriaQuery.from(Pessoa.class);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, pessoaRoot);
		
		criteriaQuery.select(pessoaRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		
		if (filtro.getPropriedadeOrdenacao() != null) {
			String nomePropriedadeOrdenacao = filtro.getPropriedadeOrdenacao();
			orderByFromEntity = pessoaRoot;
			
			if (filtro.getPropriedadeOrdenacao().contains(".")) {
				nomePropriedadeOrdenacao = nomePropriedadeOrdenacao.substring(filtro.getPropriedadeOrdenacao().indexOf(".") + 1);
			}
			
			if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.asc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			} else if (filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.desc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			}
		}
		
		TypedQuery<Pessoa> query = manager.createQuery(criteriaQuery);
		query.setFirstResult(filtro.getPrimeiroRegistro());
		query.setMaxResults(filtro.getQuantidadeRegistros());
		return query.getResultList();
	}
	
	public int quantidadeFiltrados(PessoaFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<Pessoa> pessoaRoot = criteriaQuery.from(Pessoa.class);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, pessoaRoot);
		
		criteriaQuery.select(builder.count(pessoaRoot));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Long> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult().intValue();
	}
}