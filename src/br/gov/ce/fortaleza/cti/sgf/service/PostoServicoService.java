/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.service;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.PostoServico;

/**
 * @author Deivid
 * @since 10/12/09
 */
@Repository
@Transactional
public class PostoServicoService extends BaseService<Integer, PostoServico>{

	@Transactional
	public List<PostoServico> findByPosto(Integer postoId){

		return executeResultListQuery("findByPosto", postoId);
	}
}
