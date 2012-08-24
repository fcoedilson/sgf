/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.bean;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.fortaleza.cti.sgf.entity.Rastreador;
import br.gov.ce.fortaleza.cti.sgf.service.RastreadorService;

@Scope("session")
@Component("rastreadorBean")
public class RastreadorBean extends EntityBean<String, Rastreador>{

	@Autowired
	private RastreadorService service;

	protected String retrieveEntityId(Rastreador entity) {
		return entity.getImei();
	}

	protected RastreadorService retrieveEntityService() {
		return this.service;
	}

	protected Rastreador createNewEntity() {
		Rastreador rastreador = new Rastreador();
		return rastreador;
	}


	public String save(){
		this.entity.setDataCadastro(new Date());
		this.entity.setDataAtualizacao(new Date());
		return super.save();
	}

	public String update(){
		this.entity.setDataAtualizacao(new Date());
		return super.update();
	}

}
