package br.gov.ce.fortaleza.cti.sgf.service;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SuppressWarnings("unchecked")
public abstract class BaseService<Id extends Serializable, Entity extends Serializable> {

	private static final Log logger = LogFactory.getLog(BaseService.class);

	@PersistenceContext(unitName="sgf")
	protected EntityManager entityManager;

	private Class<Entity> getPersistentClass() {
		return (Class<Entity>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	}

	@Transactional
	public Entity save(Entity entity) {
		try{
			entityManager.persist(entity);
			return entity;
		} catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Transactional
	public Entity update(Entity entity) {
		try{
			entityManager.merge(entity);
			return entity;
		} catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Transactional
	public Entity delete(Id id) {
		try {
			Entity entity = retrieve(id);
			entityManager.remove(entity);
			return entity;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Transactional(readOnly = true)
	public Entity retrieve(Id id) {
		return entityManager.find(getPersistentClass(), id);
	}

	@Transactional(readOnly = true)
	public List<Entity> retrieveAll() {
		String simpleName = getPersistentClass().getSimpleName();
		return (List<Entity>) entityManager.createQuery("SELECT e FROM " + simpleName + " e").getResultList();
	}

	@Transactional(readOnly = true)
	public List<Entity> findAll(String parameter) {
		String simpleName = getPersistentClass().getSimpleName();
		return (List<Entity>) entityManager.createQuery("SELECT e FROM " + simpleName + " e order by e." + parameter + " desc").getResultList();
	}

	@Transactional(readOnly = true)
	public List<Entity> findAllByStatus(String parameter) {
		String simpleName = getPersistentClass().getSimpleName();
		return (List<Entity>) entityManager.createQuery("SELECT e FROM " + simpleName + " e where e.status != -1 order by e." + parameter + " desc").getResultList();
	}
	
	@Transactional(readOnly = true)
	public List<Entity> findAllVeiculosAtivos(String parameter) {
		String simpleName = getPersistentClass().getSimpleName();
		return (List<Entity>) entityManager.createQuery("SELECT e FROM " + simpleName + " e where e.status != -1 order by e." + parameter + " desc").getResultList();
	}

	@Transactional(readOnly = true)
	public List<Entity> executeResultListQuery(String queryName,Serializable... parameterValues) {
		return (List<Entity>) executeQuery(queryName, parameterValues).getResultList();
	}

	@Transactional(readOnly = true)
	public List executeResultListGenericQuery(String queryName,Serializable... parameterValues) {
		return executeQuery(queryName, parameterValues).getResultList();
	}

	@Transactional(readOnly = true)
	public Entity executeSingleResultQuery(String queryName,Serializable... parameterValues) {
		try {
			return (Entity) executeQuery(queryName, parameterValues).getSingleResult();
		} catch (NoResultException e) {
			//e.printStackTrace();
			return null;
		}
	}

	@Transactional(readOnly = true)
	private Query executeQuery(String queryName, Serializable... parameterValues) {
		try {
			String simpleName = getPersistentClass().getSimpleName();
			Query query = entityManager.createNamedQuery(simpleName + "."+ queryName);
			if (parameterValues != null) {
				for (int i = 0; i < parameterValues.length; i++) {
					query.setParameter(i + 1, parameterValues[i]);
				}
			}
			return query;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Transactional(readOnly = true)
	public List<Entity> filter(List<Entity> result, String filtro) {
		try {
			if (filtro != null && filtro.length() > 0) {
				PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(getPersistentClass());
				List<Entity> remove = new ArrayList<Entity>();
				String[] params = filtro.split(" ");
				for (Entity entity : result) {
					boolean check = false;
					for (String param : params) {
						if (param != null && param.length() > 1) {
							for (PropertyDescriptor descriptor : descriptors) {
								Class clazz = descriptor.getPropertyType();
								if (clazz.equals(String.class)
										|| clazz.equals(Integer.class)
										|| clazz.equals(Double.class)
										|| clazz.equals(Long.class)
										|| clazz.equals(Short.class)
										|| clazz.equals(Short.class)
										|| clazz.equals(Boolean.class)
										|| clazz.equals(Date.class)) {
									Object value = PropertyUtils.getProperty(entity, descriptor.getName());
									if (value != null) {
										if (value.toString().toLowerCase().contains(param.toLowerCase())) {
											check = true;
										}
									}
								}
							}
						}
					}
					if (!check) {
						remove.add(entity);
					}
				}
				result.removeAll(remove);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}