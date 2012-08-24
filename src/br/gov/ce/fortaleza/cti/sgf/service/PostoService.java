/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.service;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.Posto;
import br.gov.ce.fortaleza.cti.sgf.entity.TipoCombustivel;
import br.gov.ce.fortaleza.cti.sgf.entity.TipoServico;

/**
 * @author Deivid
 * @since 25/11/09
 */
@Repository
@Transactional
public class PostoService extends BaseService<Integer, Posto> {
	
	@SuppressWarnings("unchecked")
	public List<Posto> findByCombustivel(TipoCombustivel combustivel) {
		List<Posto> postos = null;
		Query query = entityManager.createQuery("select o.posto from PostoServico o where o.tipoCombustivel.id = :id");
		query.setParameter("id", combustivel.getId());
		postos = query.getResultList();
		return postos;
	}

	@SuppressWarnings("unchecked")
	public List<TipoServico> findServicosPosto(Posto posto) {
		List<TipoServico> servicos = null;
		Query query = entityManager.createQuery("select o.tipoServico from PostoServico o where o.posto.codPosto = :id");
		query.setParameter("id", posto.getCodPosto());
		servicos = query.getResultList();
		return servicos;
	}
}
