/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.bean;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.fortaleza.cti.sgf.entity.UG;
import br.gov.ce.fortaleza.cti.sgf.service.UGService;

/**
 * @author Deivid
 * @since 21/01/2010
 */
@Scope("session")
@Component("ugBean")
public class UGBean extends EntityBean<String, UG>{

	@Autowired
	private UGService service;

	@Override
	protected UG createNewEntity() {
		return new UG();
	}

	@Override
	protected String retrieveEntityId(UG entity) {
		return new String(entity.getId());
	}

	@Override
	protected UGService retrieveEntityService() {
		return this.service;
	}

	public List<UG> getUgs(){
		List<UG> ugs = new ArrayList<UG>(service.findAll());
		return ugs;
	}

	public UGService getService() {
		return service;
	}

	public void setService(UGService service) {
		this.service = service;
	}
	public String search(){
		this.entities = service.findUASorted();
		this.entities = retrieveEntityService().filter(this.entities, filter);
		setCurrentBean(currentBeanName());
		setCurrentState(SEARCH);
		return SUCCESS;
	}
}
