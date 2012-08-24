package br.gov.ce.fortaleza.cti.sgf.conversores;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.springframework.stereotype.Service;

/**
 * Generic Entity Converter
 * 
 */
@Service("entityConverter")
public class EntityConverter implements Converter, Serializable {

	private static final long serialVersionUID = -5137676309479323480L;

	public Object getAsObject(FacesContext ctx, UIComponent component,
			String value) {
		if (value != null) {
			return this.getAttributesFrom(component).get(value);
		}
		return null;
	}

	public String getAsString(FacesContext ctx, UIComponent component,
			Object value) {

		if (value != null && !"".equals(value)) {

			Object entity = (Object) value;

			this.addAttribute(component, entity);
			
			if(loadIdentifier(entity).startsWith("0")){
				return loadIdentifier(entity); 
			}else{
				Long codigo = Long.parseLong(loadIdentifier(entity));
				if (codigo != null) {
					return String.valueOf(codigo);
				}
			}
		}

		return (String) value;
	}

	protected void addAttribute(UIComponent component, Object object) {
		String key = loadIdentifier(object);
		this.getAttributesFrom(component).put(key, object);
	}

	protected Map<String, Object> getAttributesFrom(UIComponent component) {
		return component.getAttributes();
	}

	private String loadIdentifier(Object object) {
		String id = null;
		Class<?> clazz = object.getClass();
		Field[] fs = clazz.getDeclaredFields();
		for (Field f : fs) {
			if (f.isAnnotationPresent(javax.persistence.Id.class)) {
				PropertyDescriptor descriptor;
				try {
					descriptor = new PropertyDescriptor(f.getName(), clazz);
					Method m = descriptor.getReadMethod();
					id = m.invoke(object).toString();
					break;
				} catch (IntrospectionException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		return id;
	}
}
