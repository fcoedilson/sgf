/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.service;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.RegistroVeiculo;

/**
 * @author Deivid
 * @since 20/01/2010
 */
@Transactional
@Repository
public class RegistroVeiculoService extends BaseService<Integer, RegistroVeiculo>{

}
