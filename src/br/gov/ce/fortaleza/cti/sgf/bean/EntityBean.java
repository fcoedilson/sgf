package br.gov.ce.fortaleza.cti.sgf.bean;

import java.io.Serializable;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate3.HibernateJdbcException;

import br.gov.ce.fortaleza.cti.sgf.service.BaseService;
import br.gov.ce.fortaleza.cti.sgf.util.JSFUtil;

public abstract class EntityBean<Id extends Serializable, Entity extends Serializable> extends CrudStateBean{

	protected abstract BaseService<Id, Entity> retrieveEntityService();
	protected abstract Id retrieveEntityId(Entity entity);
	protected abstract Entity createNewEntity();
	protected List<Entity> entities;
	protected Entity entity;
	protected String filter;
	protected Boolean saved;
	protected Boolean updated;
	protected Integer interval = 200;

	public String close() {
		this.entities = null;
		this.entity = null;
		this.filter = null;
		return super.closePopup();
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public Entity getEntity() {
		if (this.entity == null) {
			this.entity = createNewEntity();
		}
		return this.entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public List<Entity> getEntities() {
		return (List<Entity>) entities;
	}

	public Integer getEntitiesSize() {
		return entities == null ? -1 : entities.size();
	}

	public String prepareSave() {
		this.entity = createNewEntity();
		return super.prepareSave();
	}

	public String prepareImportSave() {
		this.entity = createNewEntity();
		return super.prepareImportSave();
	}

	public String prepareEdit(){
		this.entity = retrieveEntityService().retrieve(retrieveEntityId(this.entity));
		return super.prepareEdit();
	}
	
	public String save() {
		try{
			retrieveEntityService().save(this.entity);
			return super.save();
		} catch(HibernateJdbcException e){
			if(e.getCause().getClass().equals(ConstraintViolationException.class)){
				JSFUtil.getInstance().addErrorMessage("msg.error.operacao.invalida");
			}
			return FAIL;
		} 
//		catch(GenericJDBCException e){
//			if(e.getCause().getClass().equals(ConstraintViolationException.class)){
//				JSFUtil.getInstance().addErrorMessage("msg.error.operacao.invalida");
//			}
//			return FAIL;
//		} 
	}

	public String prepareUpdate() {
		this.entity = retrieveEntityService().retrieve(retrieveEntityId(this.entity));
		return super.prepareUpdate();
	}

	public String prepareDelete() {
		this.entity = retrieveEntityService().retrieve(retrieveEntityId(this.entity));
		return super.prepareDelete();
	}

	public String update() {
		retrieveEntityService().update(this.entity);
		return super.update();
	}

	public String delete() {
		Id id = retrieveEntityId(this.entity);
		try {
			retrieveEntityService().delete(id);
		} catch (DataIntegrityViolationException e) {
			if(e.getCause().getClass().equals(ConstraintViolationException.class)){
				JSFUtil.getInstance().addErrorMessage("msg.error.operacao.invalida");
			}
			return FAIL;
		}
		return super.delete();
	}

	public String search() {
		this.entities = retrieveEntityService().retrieveAll();
		this.entities = retrieveEntityService().filter(this.entities, this.filter);
		return super.search();
	}

	public String searchSort() {
		this.entities = retrieveEntityService().findAll("id");
		this.entities = retrieveEntityService().filter(this.entities, this.filter);
		return super.search();
	}

	public String searchStatus() {
		this.entities = retrieveEntityService().findAllByStatus("id");
		return super.search();
	}

	public String populate() {
		return SUCCESS;
	}

	public Boolean getSaved() {
		return saved;
	}

	public void setSaved(Boolean saved) {
		this.saved = saved;
	}

	public Boolean getUpdated() {
		return updated;
	}

	public void setUpdated(Boolean updated) {
		this.updated = updated;
	}
	public Integer getInterval() {
		return interval;
	}
	public void setInterval(Integer interval) {
		this.interval = interval;
	}
}