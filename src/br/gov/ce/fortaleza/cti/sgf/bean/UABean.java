package br.gov.ce.fortaleza.cti.sgf.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.fortaleza.cti.sgf.entity.UA;
import br.gov.ce.fortaleza.cti.sgf.service.UAService;
import br.gov.ce.fortaleza.cti.sgf.util.SgfUtil;

@Scope("session")
@Component("uaBean")
public class UABean extends EntityBean<String, UA>{

	@Autowired
	private UAService service;

	protected String retrieveEntityId(UA entity) {
		return entity.getId();
	}

	@Override
	protected UAService retrieveEntityService() {
		return this.service;
	}


	protected UA createNewEntity() {
		UA ua = new UA();
		return ua;
	}

	public synchronized List<UA> getUas(){
		UA ua = SgfUtil.usuarioLogado().getPessoa().getUa();
		if(ua != null){
			List<UA> uas = new ArrayList<UA>(ua.getUg().getUas());
			Collections.sort(uas, new Comparator<UA>() {
				public int compare(UA o1, UA o2) {
					return o1.getDescricao().compareTo(o2.getDescricao());
				}
			});
			return uas;
		} else {
			return new ArrayList<UA>();
		}
	}
	
	public String search(){
		this.entities = service.findUASorted();
		this.entities = retrieveEntityService().filter(this.entities, filter);
		setCurrentBean(currentBeanName());
		setCurrentState(SEARCH);
		return SUCCESS;
	}
}
