package br.gov.ce.fortaleza.cti.sgf.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.Motorista;
import br.gov.ce.fortaleza.cti.sgf.entity.Multa;
import br.gov.ce.fortaleza.cti.sgf.entity.UG;
import br.gov.ce.fortaleza.cti.sgf.entity.Veiculo;

@Repository
@Transactional
public class MultaService extends BaseService<Integer, Multa> {

	public List<Multa> findByVeiculoMotorista(String name, Integer sid){
		List<Multa> result = new ArrayList<Multa>();
		if(sid == 0){
			if(name != null){
				result = executeResultListQuery("findByPlacaVeiculo", name);
			} 
		} else if( sid == 1){
			if(name != null){
				result = executeResultListQuery("findByNomeMotorista", name);
			}
		}
		return result;
	}

	public List<Multa> findByVeiculo(Veiculo v, UG ug, Date inicio, Date fim){
		if(v != null){
			return executeResultListQuery("findByVeiculo", v.getId(), inicio, fim);
		} else {
			return executeResultListQuery("findByUG", ug.getId(), inicio, fim);
		}
	}

	public List<Multa> findByMotorista(Motorista m, UG ug, Date inicio, Date fim){
		if(m != null){
			return executeResultListQuery("findByMotorista", m.getCodMotorista(), inicio, fim);
		} else {
			return executeResultListQuery("findByUG", ug.getId(), inicio, fim);
		}
	}

	public List<Multa> findByUG(UG ug, Date inicio, Date fim){
		if(ug != null){
			return executeResultListQuery("findByUG", ug.getId(), inicio, fim);
		} else {
			return executeResultListQuery("findByPeriodo", inicio, fim);
		}
	}
}