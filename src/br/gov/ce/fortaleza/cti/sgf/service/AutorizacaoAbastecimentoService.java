/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.service;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.Abastecimento;

/**
 * @author Deivid
 * @since 11/12/09
 */
@Repository
@Transactional
public class AutorizacaoAbastecimentoService extends BaseService<Integer, Abastecimento> {

}
