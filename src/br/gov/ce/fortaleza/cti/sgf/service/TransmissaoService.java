package br.gov.ce.fortaleza.cti.sgf.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.Transmissao;

@Repository
@Transactional
public class TransmissaoService extends BaseService<Long, Transmissao> {

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Transmissao> retrieveByVeiculo(Integer veiculoId, Date dataHoraInicio, Date dataHoraFim) {
		Query query = entityManager.createQuery("SELECT t FROM Transmissao t WHERE t.veiculoId = ? AND t.dataTransmissao BETWEEN ? AND ? ORDER BY t.dataTransmissao DESC");
		query.setParameter(1, veiculoId);
		query.setParameter(2, dataHoraInicio);
		query.setParameter(3, dataHoraFim);
		return query.getResultList();
	}
	
	@Transactional(readOnly = true)
	public List<Transmissao> findByVeiculo(Integer veiculoId, Date dataHoraInicio, Date dataHoraFim) {
		return executeResultListQuery("findByVeiculo", veiculoId, dataHoraInicio, dataHoraFim);
	}

	@Transactional(readOnly = true)
	public List<Transmissao> findByVeiculoVelocidade(Integer veiculoId, Date dataInicio, Date dataFim, Float velocidade) {
		return executeResultListQuery("findByVeiculoVelocidade", veiculoId, dataInicio, dataFim, velocidade);
	}

	@Transactional(readOnly = true)
	public Map<Integer, List<Transmissao>> findByVeiculoList(List<Integer> veiculoIds, Date dataInicio, Date dataFim) {
		Map<Integer, List<Transmissao>> result = new HashMap<Integer, List<Transmissao>>();
		for (Integer veiculoId : veiculoIds) {
			result.put(veiculoId, new ArrayList<Transmissao>());
		}
		List<Transmissao> resultQuery = createVeiculoListQuery(veiculoIds, dataInicio, dataFim);
		for (Transmissao Transmissao : resultQuery) {
			result.get(Transmissao.getVeiculoId()).add(Transmissao);
		}
		return result;
	}
	
	@Transactional(readOnly = true)
	public List<Transmissao> findTransmissoes(){
		
		Query query = entityManager.createQuery("SELECT t FROM Transmissao t LEFT JOIN Veiculo v ON t.veiculoId = " +
				"v.id WHERE t.codponto IS NULL ORDER BY t.dataTransmissao LIMIT 1000");
		@SuppressWarnings("unchecked")
		List<Transmissao> result = query.getResultList();
		return result;
	}
 	
	@Transactional(readOnly = true)
	public Date dataUltTransmissao(){
		Query query = entityManager.createNamedQuery("SELECT max(t.dataTransmissao) FROM Transmissao t");
		Date d = (Date)query.getSingleResult();
		return d;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Transmissao> createVeiculoListQuery(List<Integer> veiculoIds, Date dataInicio, Date dataFim) {
		if (veiculoIds != null && veiculoIds.size() > 0) {
			String idsList = veiculoIds.toString().replaceAll("\\[", "(").replaceAll("\\]", ")");
			Query query = entityManager.createQuery("SELECT p FROM Transmissao p WHERE p.veiculoId IN " + idsList +
					" AND p.dataTransmissao between ? AND ? AND p.ponto.id > 0 ORDER BY p.dataTransmissao");
			query.setParameter(1, dataInicio);
			query.setParameter(2, dataFim);
			return (List<Transmissao>) query.getResultList();
		} else {
			return new ArrayList<Transmissao>();
		}
	}
}