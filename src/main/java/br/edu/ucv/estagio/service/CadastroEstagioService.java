package br.edu.ucv.estagio.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.edu.ucv.estagio.model.Estagio;
import br.edu.ucv.estagio.model.Pessoa;
import br.edu.ucv.estagio.model.TabelaAuditoria;
import br.edu.ucv.estagio.repository.Alunos;
import br.edu.ucv.estagio.repository.Estagios;
import br.edu.ucv.estagio.util.jpa.Transactional;

public class CadastroEstagioService implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final int maximaCargaHoraria = 40;
	
	@Inject
	private CadastroAuditoriaService cadastroAuditoriaService;
	
	@Inject
	private Alunos alunos;
	
	@Inject
	private Estagios estagios;
	
	@Transactional
	public Pessoa salvar(Pessoa pessoa, Long estagioId) throws NegocioException {
		Long ultimoId;
		
		if (estagioId == null) {
			ultimoId = estagios.ultimoId() + 1;
		} else {
			ultimoId = estagioId;
		}
		
		verificarEstagiosExitem(pessoa);
		cadastroAuditoriaService.salvar(TabelaAuditoria.ESTAGIO, ultimoId, false);
		return alunos.guardar(pessoa);
	}
	
	public void verificarEstagiosExitem(Pessoa pessoa) throws NegocioException {
		List<Estagio> estagios = pessoa.getEstagiosNaoFinalizados();
		int contadorCargaHoraria = 0;
		
		for (int i = 0; i < estagios.size(); i++) {
			int ultimoContrato = estagios.get(i).getContratos().size() - 1;
					
			contadorCargaHoraria = contadorCargaHoraria + estagios.get(i).getContratos().get(ultimoContrato).getCargaHoraria();
			
			if(i + 1 < estagios.size() && estagios.get(i).getEmpresa().equals(estagios.get(i + 1).getEmpresa())) {
				throw new NegocioException("O aluno " + pessoa.getNome() + " ja possui um estágio não finalizado cadastrado com a empresa " + estagios.get(i).getEmpresa().getNomeFantasia() + "!");
            }
		}
		
		if (contadorCargaHoraria > maximaCargaHoraria) {
			throw new NegocioException("Não é possível ultrapassar " + maximaCargaHoraria + " semanais em contratos de estágio para o mesmo aluno.");
		}
	}
}