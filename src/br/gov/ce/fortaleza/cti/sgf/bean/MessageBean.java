/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.fortaleza.cti.sgf.entity.Message;
import br.gov.ce.fortaleza.cti.sgf.service.MessageService;

@Scope("session")
@Component("messageBean")
public class MessageBean extends EntityBean<Integer, Message>{

	@Autowired
	private MessageService service;

	protected Integer retrieveEntityId(Message entity) {
		return entity.getId();
	}

	protected MessageService retrieveEntityService() {
		return this.service;
	}

	protected Message createNewEntity() {
		return new Message();
	}
}
