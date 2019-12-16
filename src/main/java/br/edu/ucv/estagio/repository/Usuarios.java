package br.edu.ucv.estagio.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

import br.edu.ucv.estagio.model.Pessoa;
import br.edu.ucv.estagio.model.Status;
import br.edu.ucv.estagio.model.Usuario;
import br.edu.ucv.estagio.repository.filter.UsuarioFilter;

public class Usuarios extends Pessoas {

	private static final long serialVersionUID = 1L;
	
	public Pessoa usuarioAtivo(String cpf) {
		try {
			return manager.createQuery("select pessoa from Pessoa pessoa inner join pessoa.usuario usuario"
				+ " where pessoa.cpf = :cpf"
				+ " and usuario.status = :status"
				, Pessoa.class)
			.setParameter("cpf", cpf)
			.setParameter("status", Status.ATIVO)
			.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	private List<Predicate> criarPredicatesParaFiltro(UsuarioFilter filtro, Root<Pessoa> pessoaRoot, From<?, ?> usuarioJoin) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();
		
		if (filtro.getId() != null) {
			predicates.add(builder.equal(pessoaRoot.get("id"), filtro.getId()));
		}
		
		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(builder.like(builder.upper(pessoaRoot.get("nome")), "%" + filtro.getNome().toUpperCase() + "%"));
		}
		
		if (StringUtils.isNotBlank(filtro.getCpf())) {
			predicates.add(builder.equal(pessoaRoot.get("cpf"), filtro.getCpf()));
		}
		
		if (filtro.getTipos() != null && filtro.getTipos().length > 0) {
			predicates.add(pessoaRoot.get("tipo").in(Arrays.asList(filtro.getTipos())));
		}
		
		if (filtro.getStatuses() != null && filtro.getStatuses().length > 0) {
			predicates.add(usuarioJoin.get("status").in(Arrays.asList(filtro.getStatuses())));
		}
		
		return predicates;
	}
	
	public List<Pessoa> filtrados(UsuarioFilter filtro) {
		From<?, ?> orderByFromEntity = null;
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Pessoa> criteriaQuery = builder.createQuery(Pessoa.class);
		Root<Pessoa> pessoaRoot = criteriaQuery.from(Pessoa.class);
		From<?, ?> usuarioJoin = (From<?, ?>) pessoaRoot.fetch("usuario", JoinType.LEFT);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, pessoaRoot, usuarioJoin);
		
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
	
	public int quantidadeFiltrados(UsuarioFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<Pessoa> pessoaRoot = criteriaQuery.from(Pessoa.class);
		Join<Pessoa, Usuario> usuarioJoin = pessoaRoot.join("usuario", JoinType.LEFT);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, pessoaRoot, usuarioJoin);
		
		criteriaQuery.select(builder.count(pessoaRoot));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Long> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult().intValue();
	}
}