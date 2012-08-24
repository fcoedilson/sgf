/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.fortaleza.cti.sgf.entity.Page;
import br.gov.ce.fortaleza.cti.sgf.service.PageService;

@Scope("session")
@Component("pageBean")
public class PageBean extends EntityBean<Integer, Page>{

	@Autowired
	private PageService service;

	protected Integer retrieveEntityId(Page entity) {
		return entity.getId();
	}

	protected PageService retrieveEntityService() {
		return this.service;
	}

	protected Page createNewEntity() {
		return new Page();
	}
}
