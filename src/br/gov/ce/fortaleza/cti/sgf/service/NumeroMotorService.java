package br.gov.ce.fortaleza.cti.sgf.service;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.NumeroMotor;

@Repository
@Transactional
public class NumeroMotorService extends BaseService<Integer, NumeroMotor> {

}