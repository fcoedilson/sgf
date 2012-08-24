/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.TipoServico;

/**
 * @author Deivid
 * @since 25/11/09	
 */
@Repository
@Transactional
public class TipoServicoService extends BaseService<Integer, TipoServico>{

	@Transactional
	public List<TipoServico> findByTipo(Integer tipo){

		return executeResultListQuery("findByTipo", tipo);
	}
	
	public synchronized List<TipoServico> findServicosManutencao(){

		List<TipoServico> result = executeResultListQuery("findByTipo", 1);

		Collections.sort(result, new Comparator<TipoServico>() {
			public int compare(TipoServico o1, TipoServico o2) {
				return o1.getDescricao().compareTo(o2.getDescricao());
			}
		});

		return result;
	}
}
