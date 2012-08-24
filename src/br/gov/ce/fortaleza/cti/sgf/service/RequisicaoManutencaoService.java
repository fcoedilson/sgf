package br.gov.ce.fortaleza.cti.sgf.service;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.ItemRequisicao;
import br.gov.ce.fortaleza.cti.sgf.entity.RequisicaoManutencao;
import br.gov.ce.fortaleza.cti.sgf.entity.UG;
import br.gov.ce.fortaleza.cti.sgf.entity.Veiculo;

@Repository
@Transactional
public class RequisicaoManutencaoService extends BaseService<Integer, RequisicaoManutencao> {

	@Transactional(readOnly = true)
	public List<RequisicaoManutencao> findByStatus(Integer vid, Integer stid){
		List<RequisicaoManutencao> list;
		if(vid == null && stid == 0){
			list = super.retrieveAll();
		} else if(vid == null && stid == 1){
			list = executeResultListQuery("findByStatus", -1);
		} else if(vid == null && stid == 2){
			list = executeResultListQuery("findByStatus", 1);
		} else if(vid != null && stid == 0){
			list = executeResultListQuery("findByVeiculo", vid);
		} else if(vid != null && stid == 1){
			list = executeResultListQuery("findByVeiculoStatus", vid, -1);
		} else if(vid != null && stid == 2){
			list = executeResultListQuery("findByVeiculoStatus", vid, 1);
		} else {
			return null;
		}
		return list;
	}

	@Transactional(readOnly = true)
	public boolean findByVeiculoStatus(Integer vid, Integer stid){
		Query query = entityManager.createQuery("Select r from RequisicaoManutencao r where r.veiculo.id = ? and r.status = ?");
		query.setParameter(1, vid);
		query.setParameter(2, stid);
		return query.getResultList().size() > 0;
	}

	@SuppressWarnings("unchecked")
	public List<RequisicaoManutencao> findByPeriodo(String ug, Integer veiculo, Date dataInicio, Date dataFim){

		StringBuffer str = new StringBuffer("select o from RequisicaoManutencao o where o.dataSaida between :di and :df");
		if(ug != null){
			str.append(" and o.veiculo.ua.ug.id = :ug");
		}
		if(veiculo != null){
			str.append(" and o.veiculo.id = :veiculo");
		}
		str.append(" order by o.dataSaida asc");
		Query query = entityManager.createQuery(str.toString());
		query.setParameter("di", dataInicio);
		query.setParameter("df", dataFim);
		if(ug != null){
			query.setParameter("ug", ug);
		}
		if(veiculo != null){
			query.setParameter("veiculo", veiculo);
		}
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<RequisicaoManutencao> findByUG(String ug){

		StringBuffer str = new StringBuffer("select o from RequisicaoManutencao o where o.status > -1");
		if(ug != null){
			str.append(" and o.veiculo.ua.ug.id = :ug");
		}

		str.append(" order by o.dataSaida desc");
		Query query = entityManager.createQuery(str.toString());

		if(ug != null){
			query.setParameter("ug", ug);
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<RequisicaoManutencao> findByUgStatus(UG ug, Date dataInicio, Date dataFim){

		StringBuffer str = new StringBuffer("SELECT o FROM RequisicaoManutencao o WHERE o.dataSaida between :dtInicio AND :dtFim AND o.dataRetorno is null");
		if(ug != null){
			str.append(" AND o.veiculo.ua.ug.id = :ug");
		}
		str.append(" ORDER BY o.dataSaida ASC");
		Query query = entityManager.createQuery(str.toString());
		query.setParameter("dtInicio", dataInicio);
		query.setParameter("dtFim", dataFim);
		if(ug != null){
			query.setParameter("ug", ug.getId());
		}
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<RequisicaoManutencao> findManutencaoSemRetorno(UG ug, Date dataInicio, Date dataFim){
		
		Date dataAtual = new Date();
		StringBuffer str = new StringBuffer("SELECT o FROM RequisicaoManutencao o WHERE o.dataSaida between :dtInicio " +
				"AND :dtFim AND (o.dataFim <= :dtAtual AND o.dataRetorno is null)");
		if(ug != null){
			str.append(" AND o.veiculo.ua.ug.id = :ug");
		}
		str.append(" ORDER BY o.dataSaida ASC");
		Query query = entityManager.createQuery(str.toString());
		query.setParameter("dtInicio", dataInicio);
		query.setParameter("dtFim", dataFim);
		query.setParameter("dtAtual", dataAtual);
		if(ug != null){
			query.setParameter("ug", ug.getId());
		}
		return query.getResultList();
	}

	public  List<RequisicaoManutencao> findByVeiculo(Integer id){
		List<RequisicaoManutencao>  result = executeResultListQuery("findByVeiculo", id);
		return result;
	}

	public  List<RequisicaoManutencao> findByVeiculoLivre(Integer vid, Date dataInicio, Date dataFim){
		List<RequisicaoManutencao>  result = executeResultListQuery("findByVeiculoLivre", vid, dataInicio, dataInicio, dataInicio, dataFim, dataFim, dataFim);
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<UG> findUgsByManutencao(String ug, Integer veiculo, Date dataInicio, Date dataFim) {
		List<UG> orgaos = null;
		StringBuffer str = new StringBuffer("select distinct(o.veiculo.ua.ug) from RequisicaoManutencao o where o.dataSaida between :di and :df");
		if(ug != null){
			str.append(" and o.veiculo.ua.ug.id = :ug");
		}
		if(veiculo != null){
			str.append(" and o.veiculo.id = :veiculo");
		}
		str.append(" order by o.veiculo.ua.ug.descricao ");
		Query query = entityManager.createQuery(str.toString());
		query.setParameter("di", dataInicio);
		query.setParameter("df", dataFim);
		if(ug != null){
			query.setParameter("ug", ug);
		}
		if(veiculo != null){
			query.setParameter("veiculo", veiculo);
		}
		orgaos = query.getResultList(); 
		return orgaos;
	}

	@SuppressWarnings("unchecked")
	public List<Veiculo> findVeiculosUGByManutencao(String ug, Integer veiculo, Date dataInicio, Date dataFim) {
		List<Veiculo> veiculos = null;
		StringBuffer str = new StringBuffer("select distinct(o.veiculo) from RequisicaoManutencao o where o.dataSaida between ? and ?");
		if(ug != null){
			str.append(" and o.veiculo.ua.ug.id = '"+ug+"'");
		}
		if(veiculo != null){
			str.append(" and o.veiculo.id = "+veiculo);
		}
		str.append(" order by o.veiculo.placa ");
		Query query = entityManager.createQuery(str.toString());
		query.setParameter(1, dataInicio);
		query.setParameter(2, dataFim);
		
		veiculos = query.getResultList(); 
		return veiculos;
	}

	@SuppressWarnings("unchecked")
	public List<RequisicaoManutencao> findManutencaoVeiculos(String ug, Integer veiculo, Date dataInicio, Date dataFim) {
		List<RequisicaoManutencao> manutencoes = null;
		StringBuffer str = new StringBuffer("select o from RequisicaoManutencao o where o.dataSaida between :di and :df");
		if(ug != null){
			str.append(" and o.veiculo.ua.ug.id = :ug");
		}
		if(veiculo != null){
			str.append(" and o.veiculo.id = :veiculo");
		}
		str.append(" order by o.dataInicio ");
		Query query = entityManager.createQuery(str.toString());
		query.setParameter("di", dataInicio);
		query.setParameter("df", dataFim);
		if(ug != null){
			query.setParameter("ug", ug);
		}
		if(veiculo != null){
			query.setParameter("veiculo", veiculo);
		}
		manutencoes = query.getResultList(); 
		return manutencoes;
	}

	@SuppressWarnings("unchecked")
	public List<ItemRequisicao> findItensRequisicao(Integer id) {
		List<ItemRequisicao> itens = null;
		StringBuffer str = new StringBuffer("select o from ItemRequisicao o where o.requisicaoManutencao.id = ?");
		Query query = entityManager.createQuery(str.toString());
		if (id != null) {
			query.setParameter(1, id);
		}
		itens = query.getResultList();
		return itens;
	}
}
