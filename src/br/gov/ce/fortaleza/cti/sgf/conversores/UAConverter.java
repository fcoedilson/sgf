/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.ce.fortaleza.cti.sgf.entity.UA;
import br.gov.ce.fortaleza.cti.sgf.service.UAService;

/**
 * @author Deivid
 * @since 08/01/2010
 */
public class UAConverter implements Converter {
	
	@Autowired
	private UAService service;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String str) {
		if(str != null && !str.equals("")){
			UA ua = service.retrieve(str);
			return ua;
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {

		if(obj == null || obj.equals("")){
			return null;
		}else{
			UA ua = (UA) obj;
			return String.valueOf(ua.getId());
		}
	}
}
