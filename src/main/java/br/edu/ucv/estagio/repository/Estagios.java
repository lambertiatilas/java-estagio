package br.edu.ucv.estagio.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
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

import br.edu.ucv.estagio.model.PessoaEstagio;
import br.edu.ucv.estagio.model.EmpresaEstagio;
import br.edu.ucv.estagio.model.Contrato;
import br.edu.ucv.estagio.model.Empresa;
import br.edu.ucv.estagio.model.Estagio;
import br.edu.ucv.estagio.model.Pessoa;
import br.edu.ucv.estagio.repository.filter.EstagioFilter;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jpa.Transactional;

public class Estagios implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	@Transactional
	public void remover(Estagio estagio) throws NegocioException {
		try {
			estagio = porId(estagio.getId());
			manager.remove(estagio);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Estágio não pode ser excluído.");
		}
	}
	
	public Estagio porId(Long id) {
		return manager.find(Estagio.class, id);
	}
	
	public Long ultimoId() {
		Long ultimoId = manager.createQuery("select max(estagio.id) from Estagio estagio", Long.class).getSingleResult();
		
		if (ultimoId == null) {
			return new Long(0);
		}
		
		return ultimoId;
	}
	
	private List<Predicate> criarPredicatesParaFiltro(EstagioFilter filtro, Root<Estagio> estagioRoot, From<?, ?> alunoJoin, From<?, ?> pessoaJoin, From<?, ?> concedenteJoin, From<?, ?> empresaJoin, From<?, ?> contratoJoin) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();
		
		if (filtro.getId() != null) {
			predicates.add(builder.equal(estagioRoot.get("id"), filtro.getId()));
		}
		
		if (filtro.getDataInicioDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(contratoJoin.get("dataInicio"), filtro.getDataInicioDe()));
		}
		
		if (filtro.getDataInicioAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(contratoJoin.get("dataInicio"), filtro.getDataInicioAte()));
		}
		
		if (filtro.getDataFimDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(contratoJoin.get("dataFim"), filtro.getDataFimDe()));
		}
		
		if (filtro.getDataFimAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(contratoJoin.get("dataFim"), filtro.getDataFimAte()));
		}
		
		if (StringUtils.isNotBlank(filtro.getNomeAluno())) {
			predicates.add(builder.like(builder.upper(alunoJoin.get("nome")), "%" + filtro.getNomeAluno().toUpperCase() + "%"));
		}
		
		if (filtro.getMatriculaAluno() != null) {
			predicates.add(builder.equal(alunoJoin.get("matricula"), filtro.getMatriculaAluno()));
		}
		
		if (StringUtils.isNotBlank(filtro.getRazaoSocialEmpresa())) {
			predicates.add(builder.like(builder.upper(empresaJoin.get("razaoSocial")), "%" + filtro.getRazaoSocialEmpresa().toUpperCase() + "%"));
		}
		
		if (StringUtils.isNotBlank(filtro.getCnpjEmpresa())) {
			predicates.add(builder.equal(empresaJoin.get("cnpj"), filtro.getNomeAluno()));
		}
		
		if (filtro.getStatus() != null) {
			predicates.add(estagioRoot.get("status").in(filtro.getStatus()));
		}
		
		return predicates;
	}
	
	public List<Estagio> filtrados(EstagioFilter filtro) {
		From<?, ?> orderByFromEntity = null;
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Estagio> criteriaQuery = builder.createQuery(Estagio.class);
		Root<Estagio> estagioRoot = criteriaQuery.from(Estagio.class);
		From<?, ?> alunoJoin = (From<?, ?>) estagioRoot.fetch("aluno", JoinType.INNER);
		From<?, ?> pessoaJoin = (From<?, ?>) alunoJoin.fetch("pessoa", JoinType.INNER);
		From<?, ?> concedenteJoin = (From<?, ?>) estagioRoot.fetch("concedente", JoinType.INNER);
		From<?, ?> empresaJoin = (From<?, ?>) concedenteJoin.fetch("empresa", JoinType.INNER);
		From<?, ?> contratoJoin = (From<?, ?>) estagioRoot.fetch("contratos", JoinType.INNER);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, estagioRoot, alunoJoin, pessoaJoin, concedenteJoin, empresaJoin, contratoJoin);
		
		criteriaQuery.select(estagioRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		
		if (filtro.getPropriedadeOrdenacao() != null) {
			String nomePropriedadeOrdenacao = filtro.getPropriedadeOrdenacao();
			orderByFromEntity = estagioRoot;
			
			if (filtro.getPropriedadeOrdenacao().contains(".")) {
				nomePropriedadeOrdenacao = nomePropriedadeOrdenacao.substring(filtro.getPropriedadeOrdenacao().indexOf(".") + 1);
			}
			
			if (filtro.getPropriedadeOrdenacao().startsWith("nomeAluno.")) {
				orderByFromEntity = alunoJoin;
			}
			
			if (filtro.getPropriedadeOrdenacao().startsWith("razaoSocialEmpresa.")) {
				orderByFromEntity = empresaJoin;
			}
			
			if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.asc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			} else if (filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.desc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			}
		}
		
		TypedQuery<Estagio> query = manager.createQuery(criteriaQuery);
		query.setFirstResult(filtro.getPrimeiroRegistro());
		query.setMaxResults(filtro.getQuantidadeRegistros());
		return query.getResultList();
	}
	
	public int quantidadeFiltrados(EstagioFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<Estagio> estagioRoot = criteriaQuery.from(Estagio.class);
		Join<Estagio, PessoaEstagio> alunoJoin = estagioRoot.join("aluno", JoinType.INNER);
		Join<PessoaEstagio, Pessoa> pessoaJoin = alunoJoin.join("pessoa", JoinType.INNER);
		Join<Estagio, EmpresaEstagio> concedenteJoin = estagioRoot.join("concedente", JoinType.INNER);
		Join<EmpresaEstagio, Empresa> empresaJoin = concedenteJoin.join("empresa", JoinType.INNER);
		Join<Estagio, Contrato> contratoJoin = estagioRoot.join("contratos", JoinType.INNER);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, estagioRoot, alunoJoin, pessoaJoin, concedenteJoin, empresaJoin, contratoJoin);
		
		criteriaQuery.select(builder.count(estagioRoot));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Long> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult().intValue();
	}
}