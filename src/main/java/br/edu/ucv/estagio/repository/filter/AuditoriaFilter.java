package br.edu.ucv.estagio.repository.filter;

import java.io.Serializable;
import java.util.Date;

import br.edu.ucv.estagio.model.AcaoAuditoria;
import br.edu.ucv.estagio.model.TabelaAuditoria;
import br.edu.ucv.estagio.util.date.DateUtil;

public class AuditoriaFilter extends OrdenacaoFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private TabelaAuditoria tabelaNome;
	private Long tabelaId;
	private AcaoAuditoria acao;
	private Date dataCriacaoDe;
	private Date dataCriacaoAte;
	private String criadoPor;
	private Date dataModificacaoDe;
	private Date dataModificacaoAte;
	private String modificadoPor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TabelaAuditoria getTabelaNome() {
		return tabelaNome;
	}

	public void setTabelaNome(TabelaAuditoria tabelaNome) {
		this.tabelaNome = tabelaNome;
	}

	public Long getTabelaId() {
		return tabelaId;
	}

	public void setTabelaId(Long tabelaId) {
		this.tabelaId = tabelaId;
	}

	public AcaoAuditoria getAcao() {
		return acao;
	}

	public void setAcao(AcaoAuditoria acao) {
		this.acao = acao;
	}

	public Date getDataCriacaoDe() {
		return dataCriacaoDe;
	}

	public void setDataCriacaoDe(Date dataCriacaoDe) {
		this.dataCriacaoDe = dataCriacaoDe;
	}

	public Date getDataCriacaoAte() {
		return dataCriacaoAte;
	}

	public void setDataCriacaoAte(Date dataCriacaoAte) {
		this.dataCriacaoAte = DateUtil.maisUmDia(dataCriacaoAte);
	}

	public String getCriadoPor() {
		return criadoPor;
	}

	public void setCriadoPor(String criadoPor) {
		this.criadoPor = criadoPor;
	}

	public Date getDataModificacaoDe() {
		return dataModificacaoDe;
	}

	public void setDataModificacaoDe(Date dataModificacaoDe) {
		this.dataModificacaoDe = dataModificacaoDe;
	}

	public Date getDataModificacaoAte() {
		return dataModificacaoAte;
	}

	public void setDataModificacaoAte(Date dataModificacaoAte) {
		this.dataModificacaoAte = DateUtil.maisUmDia(dataModificacaoAte);
	}

	public String getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(String modificadoPor) {
		this.modificadoPor = modificadoPor;
	}
}