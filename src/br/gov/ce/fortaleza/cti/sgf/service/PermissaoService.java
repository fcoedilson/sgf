package br.gov.ce.fortaleza.cti.sgf.service;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.Permissao;

@Repository
@Transactional
public class PermissaoService extends BaseService<Integer, Permissao> {

}