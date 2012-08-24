package br.gov.ce.fortaleza.cti.sgf.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.ItemRequisicao;

@Repository
@Transactional
public class ItemRequisicaoService extends BaseService<Integer, ItemRequisicao> {

	@Transactional(readOnly = true)
	public List<ItemRequisicao> findByIdRequisicao(Integer requisicaoId){

		List<ItemRequisicao> list = executeResultListQuery("findByIdRequisicao", requisicaoId);

		return list;
	}

	@Transactional(readOnly = true)
	public void deleteByRequisicao(Integer requisicaoId){

		Query  query = this.entityManager.createQuery("delete from ItemRequisicao where requisicaoManutencao.id = ?");

		query.setParameter(1, requisicaoId);

		query.executeUpdate();
	}

	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<ItemRequisicao> findByVeiculosTipoServico(List<Integer> veiculoIds, Integer tipoServicoId){

		if (veiculoIds != null && veiculoIds.size() > 0) {

			String idsList = veiculoIds.toString().replaceAll("\\[", "(").replaceAll("\\]", ")");

			Query query = entityManager.createQuery("SELECT i FROM ItemRequisicao i WHERE i.tipoServico.id = ? and i.requisicaoManutencao.veiculo.id IN " + idsList);
			
			query.setParameter(1, tipoServicoId);

			return query.getResultList();

		} else {

			return new ArrayList<ItemRequisicao>();
		}
	}

	@Transactional(readOnly = true)
	public List<ItemRequisicao> findByTipoServico(Integer tsid){

		List<ItemRequisicao> list = executeResultListQuery("findByTipoServico", tsid);

		return list;
	}
}
