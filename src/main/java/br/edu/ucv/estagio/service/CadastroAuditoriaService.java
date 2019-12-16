package br.edu.ucv.estagio.service;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import br.edu.ucv.estagio.model.AcaoAuditoria;
import br.edu.ucv.estagio.model.Auditoria;
import br.edu.ucv.estagio.model.TabelaAuditoria;
import br.edu.ucv.estagio.repository.Auditorias;
import br.edu.ucv.estagio.security.Seguranca;
import br.edu.ucv.estagio.util.jpa.Transactional;

public class CadastroAuditoriaService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Seguranca seguranca;
	
	@Inject
	private Auditorias auditorias;
	
	@Transactional
	public void salvar(TabelaAuditoria tabelaNome, Long tabelaId, boolean exclusao) throws NegocioException {
		boolean permitirAuditoria = true;
		
		if (!permitirAuditoria) {
			return;
		}
		
		Auditoria auditoria = auditorias.existe(tabelaNome, tabelaId);
		
		if (auditoria != null) {
			if (exclusao) {
				auditoria.setAcao(AcaoAuditoria.EXCLUSAO);
			} else {
				auditoria.setAcao(AcaoAuditoria.ATUALIZACAO);
				auditoria.setQuantidadeSalva(auditoria.getQuantidadeSalva() + new Long(1));
			}	
		} else {
			auditoria = new Auditoria();
			auditoria.setTabelaNome(tabelaNome);
			auditoria.setTabelaId(tabelaId);
			auditoria.setAcao(AcaoAuditoria.INSERCAO);
			auditoria.setDataCriacao(new Date());
			auditoria.setCriadoPor(seguranca.getAutenticado());
			auditoria.setQuantidadeSalva(new Long(1));
		}
		
		auditoria.setDataModificacao(new Date());
		auditoria.setModificadoPor(seguranca.getAutenticado());
		auditorias.guardar(auditoria);
	}
}