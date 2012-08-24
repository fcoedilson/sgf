/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.bean;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.fortaleza.cti.sgf.entity.TipoCombustivel;
import br.gov.ce.fortaleza.cti.sgf.service.TipoCombustivelService;

/**
 * @author Deivid
 *	
 */
@Scope("session")
@Component("combustivelBean")
public class CombustivelBean extends EntityBean<Integer, TipoCombustivel>{

	@Autowired
	private TipoCombustivelService service;

	@Override
	protected TipoCombustivel createNewEntity() {

		return new TipoCombustivel();
	}

	@Override
	protected Integer retrieveEntityId(TipoCombustivel entity) {

		return entity.getId();
	}

	@Override
	protected TipoCombustivelService retrieveEntityService() {

		return getService();
	}


	public List<TipoCombustivel> getCombustiveis(){
		
		List<TipoCombustivel> tipos = new ArrayList<TipoCombustivel>(service.retrieveAll());

		return tipos;
	}

	public void setService(TipoCombustivelService service) {
		this.service = service;
	}

	public TipoCombustivelService getService() {
		return service;
	}
}
