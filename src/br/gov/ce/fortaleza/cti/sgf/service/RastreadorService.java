package br.gov.ce.fortaleza.cti.sgf.service;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.Rastreador;

@Repository
@Transactional
public class RastreadorService extends BaseService<String, Rastreador>{

}
