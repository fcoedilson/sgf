package br.gov.ce.fortaleza.cti.sgf.service;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.Parametro;

@Repository
@Transactional
public class ParametroService extends BaseService<Integer, Parametro> {

	public List<Parametro> findByTipo(String tipo){
		List<Parametro> result = executeResultListQuery("findByTipo", tipo);
		return result ;
	}
	
	public Parametro findByNome(String nome){
		return executeSingleResultQuery("findByNome", nome) ;
	}
}