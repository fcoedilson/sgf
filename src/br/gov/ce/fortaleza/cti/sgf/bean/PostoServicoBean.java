/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.fortaleza.cti.sgf.entity.PostoServico;
import br.gov.ce.fortaleza.cti.sgf.service.BaseService;
import br.gov.ce.fortaleza.cti.sgf.service.PostoServicoService;

/**
 * @author Deivid
 *
 */
@Scope("session")
@Component("postoServicoBean")
public class PostoServicoBean extends EntityBean<Integer, PostoServico>{
	
	@Autowired
	private PostoServicoService service;

	@Override
	protected PostoServico createNewEntity() {
		// TODO Auto-generated method stub
		return new PostoServico();
	}

	@Override
	protected Integer retrieveEntityId(PostoServico entity) {
		// TODO Auto-generated method stub
		return entity.getId();
	}

	@Override
	protected BaseService<Integer, PostoServico> retrieveEntityService() {
		// TODO Auto-generated method stub
		return service;
	}

	public void setService(PostoServicoService service) {
		this.service = service;
	}

	public PostoServicoService getService() {
		return service;
	}

}
