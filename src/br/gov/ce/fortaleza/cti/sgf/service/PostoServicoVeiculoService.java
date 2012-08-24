/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.service;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.PostoServicoVeiculo;
import br.gov.ce.fortaleza.cti.sgf.entity.Veiculo;

/**
 * @author Deivid
 *
 */
@Repository
@Transactional
public class PostoServicoVeiculoService extends BaseService<Integer, PostoServicoVeiculo>{

	@SuppressWarnings("unchecked")
	public List<PostoServicoVeiculo> findPostoServico(Veiculo veiculo) {
		List<PostoServicoVeiculo> postoServicoVeiculos = null;
		
		Query query = entityManager.createQuery("select o from PostoServicoVeiculo o where o.veiculo = ?");
		query.setParameter(1, veiculo);
		
		postoServicoVeiculos = query.getResultList();
		
		return postoServicoVeiculos;
	}

}
