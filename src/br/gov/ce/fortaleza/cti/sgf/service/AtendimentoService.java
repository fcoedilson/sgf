package br.gov.ce.fortaleza.cti.sgf.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.Abastecimento;
import br.gov.ce.fortaleza.cti.sgf.entity.AtendimentoAbastecimento;
import br.gov.ce.fortaleza.cti.sgf.entity.UG;
import br.gov.ce.fortaleza.cti.sgf.entity.Veiculo;

/**
 * 
 * @author Deivid
 * @since 17/01/2010
 *
 */
@Transactional
@Repository
public class AtendimentoService extends BaseService<Integer, AtendimentoAbastecimento>{


	/**
	 * Consulta abastecimento por UG, Veículos, período(data inicial - data final)
	 * @param ug
	 * @param veiculo
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	@Autowired
	private UGService ugservice;
	
	@SuppressWarnings("unchecked")
	public List<AtendimentoAbastecimento> findByPeriodo(String ug, String veiculo, Date dataInicio, Date dataFim){

		StringBuffer str = new StringBuffer("select o from AtendimentoAbastecimento o where o.horaAtendimento between ? and ?");
		if(ug != null){
			str.append(" and o.abastecimento.veiculo.ua.ug.id = :ug");
		}
		if(veiculo != null){
			str.append(" and o.abastecimento.veiculo.id = :veiculo");
		}
		str.append(" order by o.horaAtendimento asc");
		Query query = entityManager.createQuery(str.toString());
		query.setParameter(1, dataInicio);
		query.setParameter(2, dataFim);
		if(ug != null){
			query.setParameter("ug", ug);
		}
		if(veiculo != null){
			query.setParameter("veiculo", veiculo);
		}
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<AtendimentoAbastecimento> findListAbastecimentosVeiculo(UG ug, Veiculo veiculo, Date dataInicio, Date dataFim){
		StringBuffer str = new StringBuffer("select o from AtendimentoAbastecimento o where o.abastecimento.dataAutorizacao between ? and ?");
		
		if(ug != null){
			str.append(" and o.abastecimento.veiculo.ua.ug.id = :ugid");
		}
		if(veiculo != null){
			str.append(" and o.abastecimento.veiculo.id = :vid");
		}
		
		str.append(" and o.abastecimento.veiculo.status > -1 and o.status = 0 ");
		str.append(" order by o.hora asc");
		Query query = entityManager.createQuery(str.toString());
		query.setParameter(1, dataInicio);
		query.setParameter(2, dataFim);
		
		if(ug != null){
			query.setParameter("ugid", ug.getId());
		}
		if(veiculo != null){
			query.setParameter("vid", veiculo.getId());
		}
		List<AtendimentoAbastecimento> result = query.getResultList();
		return result;
	}

	public Map<UG, List<AtendimentoAbastecimento>> findHashAbastecimentosVeiculo(UG ug, Veiculo veiculo, Date dataInicio, Date dataFim){

		Map<UG, List<AtendimentoAbastecimento>> mapAbastecimentosPorVeiculo = new HashMap<UG, List<AtendimentoAbastecimento>>();
		List<AtendimentoAbastecimento> result = findListAbastecimentosVeiculo(ug, veiculo, dataInicio, dataFim);
		
		for (AtendimentoAbastecimento atend : result) {
			UG ugSol = atend.getAbastecimento().getVeiculo().getUa().getUg();
			if(mapAbastecimentosPorVeiculo.get(ugSol) == null){
				List<AtendimentoAbastecimento> novo = new ArrayList<AtendimentoAbastecimento>();
				novo.add(atend);
				mapAbastecimentosPorVeiculo.put(ugSol, novo);
			} else {
				mapAbastecimentosPorVeiculo.get(ugSol).add(atend);
			}
		}
		return mapAbastecimentosPorVeiculo;
	}

	/**
	 * Busca a quantidade abastecida no mês pelo veículo
	 * @param veiculo
	 * @return
	 */
	public Double findQtdAbastecida(Veiculo veiculo) {
		Double result = 0.0;
		Query query = entityManager.createNamedQuery("AtendimentoAbastecimento.findCota");
		query.setParameter("veiculo", veiculo);
		query.setParameter("mes", new Date().getMonth());
		result = (Double) query.getSingleResult();
		return result;
	}


	@SuppressWarnings("unchecked")
	public List<UG> findAtendimentoByUG(String ug, String veiculo, Date dataInicio, Date dataFim) {
		StringBuffer str = new StringBuffer("select distinct(o.abastecimento.veiculo.ua.ug) from AtendimentoAbastecimento o where o.data between ? and ?");
		if(ug != null){
			str.append(" and o.abastecimento.veiculo.ua.ug.id = '"+ug+"'");
		}
		if(veiculo != null){
			str.append(" and o.abastecimento.veiculo.id = "+veiculo);
		}
		str.append(" and o.abastecimento.veiculo.status > -1 and o.status = 0");
		str.append(" order by o.abastecimento.veiculo.ua.ug.descricao");
		Query query = entityManager.createQuery(str.toString());
		query.setParameter(1, dataInicio);
		query.setParameter(2, dataFim);
		return query.getResultList();
	}


	@SuppressWarnings("unchecked")
	public List<Veiculo> findAtendimentoByVeiculo(String ug, String veiculo, Date dataInicio, Date dataFim) {
		StringBuffer str = new StringBuffer("select distinct(o.abastecimento.veiculo) from AtendimentoAbastecimento o where o.data between ? and ?");
		if(ug != null){
			str.append(" and o.abastecimento.veiculo.ua.ug.id = '"+ug+"'");
		}
		if(veiculo != null){
			str.append(" and o.abastecimento.veiculo.id = "+veiculo);
		}
		str.append(" and o.abastecimento.veiculo.status > -1 and o.status = 0");
		str.append(" order by o.abastecimento.veiculo.placa");
		Query query = entityManager.createQuery(str.toString());
		query.setParameter(1, dataInicio);
		query.setParameter(2, dataFim);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Abastecimento> findAtendimentoByVeiculoAbastecimento(String ug, String veiculo, Date dataInicio, Date dataFim) {
		StringBuffer str = new StringBuffer("select distinct(o.abastecimento) from AtendimentoAbastecimento o where o.hora between ? and ?");
		if(ug != null){
			str.append(" and o.abastecimento.veiculo.ua.ug.id = '"+ug+"'");
		}
		if(veiculo != null){
			str.append(" and o.abastecimento.veiculo.id = "+veiculo);
		}
		str.append(" and o.abastecimento.veiculo.status > -1 and o.status = 1");
		str.append(" order by o.data");
		Query query = entityManager.createQuery(str.toString());
		query.setParameter(1, dataInicio);
		query.setParameter(2, dataFim);
		return query.getResultList();
	}
}
