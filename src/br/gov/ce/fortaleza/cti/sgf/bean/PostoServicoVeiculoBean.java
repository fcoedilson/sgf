/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.fortaleza.cti.sgf.entity.PostoServicoVeiculo;
import br.gov.ce.fortaleza.cti.sgf.service.BaseService;
import br.gov.ce.fortaleza.cti.sgf.service.PostoServicoVeiculoService;

/**
 * @author Deivid
 * @since 10/12/09
 */
@Scope("session")
@Component("postoServicoVeiculoBean")
public class PostoServicoVeiculoBean extends EntityBean<Integer, PostoServicoVeiculo>{
	
	@Autowired
	private PostoServicoVeiculoService service;

	@Override
	protected PostoServicoVeiculo createNewEntity() {
		// TODO Auto-generated method stub
		return new PostoServicoVeiculo();
	}

	@Override
	protected Integer retrieveEntityId(PostoServicoVeiculo entity) {
		// TODO Auto-generated method stub
		return entity.getId();
	}

	@Override
	protected BaseService<Integer, PostoServicoVeiculo> retrieveEntityService() {
		// TODO Auto-generated method stub
		return this.service;
	}

	public void setService(PostoServicoVeiculoService service) {
		this.service = service;
	}

	public PostoServicoVeiculoService getService() {
		return service;
	}

}
