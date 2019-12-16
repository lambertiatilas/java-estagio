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

import br.edu.ucv.estagio.model.Empresa;
import br.edu.ucv.estagio.repository.filter.EmpresaFilter;
import br.edu.ucv.estagio.service.NegocioException;
import br.edu.ucv.estagio.util.jpa.Transactional;

public class Empresas implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public Empresa guardar(Empresa empresa) {
		return manager.merge(empresa);
	}
	
	@Transactional
	public void remover(Empresa empresa) throws NegocioException {
		try {
			empresa = porId(empresa.getId());
			manager.remove(empresa);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Empresa não pode ser excluído.");
		}
	}
	
	public Empresa porId(Long id) {
		return manager.find(Empresa.class, id);
	}
	
	public Empresa porRazaoSocial(String razaoSocial) {
		try {
			return manager.createQuery("from Empresa where upper(razaoSocial) = :razaoSocial", Empresa.class)
				.setParameter("razaoSocial", razaoSocial.toUpperCase())
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public Empresa porCnpj(String cnpj) {
		try {
			return manager.createQuery("from Empresa where cnpj = :cnpj", Empresa.class)
				.setParameter("cnpj", cnpj)
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Empresa> empresas() {
		return manager.createQuery("from Empresa", Empresa.class).getResultList();
	}
	
	private List<Predicate> criarPredicatesParaFiltro(EmpresaFilter filtro, Root<Empresa> empresaRoot) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();
		
		if (filtro.getId() != null) {
			predicates.add(builder.equal(empresaRoot.get("id"), filtro.getId()));
		}
		
		if (StringUtils.isNotBlank(filtro.getRazaoSocial())) {
			predicates.add(builder.like(builder.upper(empresaRoot.get("razaoSocial")), "%" + filtro.getRazaoSocial().toUpperCase() + "%"));
		}
		
		if (StringUtils.isNotBlank(filtro.getNomeFantasia())) {
			predicates.add(builder.like(builder.upper(empresaRoot.get("nomeFantasia")), "%" + filtro.getNomeFantasia().toUpperCase() + "%"));
		}
		
		if (StringUtils.isNotBlank(filtro.getCnpj())) {
			predicates.add(builder.equal(empresaRoot.get("cnpj"), filtro.getCnpj()));
		}
		
		return predicates;
	}
	
	public List<Empresa> filtradas(EmpresaFilter filtro) {
		From<?, ?> orderByFromEntity = null;
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Empresa> criteriaQuery = builder.createQuery(Empresa.class);
		Root<Empresa> empresaRoot = criteriaQuery.from(Empresa.class);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, empresaRoot);
		
		criteriaQuery.select(empresaRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		
		if (filtro.getPropriedadeOrdenacao() != null) {
			String nomePropriedadeOrdenacao = filtro.getPropriedadeOrdenacao();
			orderByFromEntity = empresaRoot;
			
			if (filtro.getPropriedadeOrdenacao().contains(".")) {
				nomePropriedadeOrdenacao = nomePropriedadeOrdenacao.substring(filtro.getPropriedadeOrdenacao().indexOf(".") + 1);
			}
			
			if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.asc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			} else if (filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.desc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			}
		}
		
		TypedQuery<Empresa> query = manager.createQuery(criteriaQuery);
		query.setFirstResult(filtro.getPrimeiroRegistro());
		query.setMaxResults(filtro.getQuantidadeRegistros());
		return query.getResultList();
	}
	
	public int quantidadeFiltrados(EmpresaFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<Empresa> empresaRoot = criteriaQuery.from(Empresa.class);
		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, empresaRoot);
		
		criteriaQuery.select(builder.count(empresaRoot));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Long> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult().intValue();
	}
}