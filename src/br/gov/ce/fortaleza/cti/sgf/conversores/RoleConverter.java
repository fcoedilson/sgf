/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.ce.fortaleza.cti.sgf.entity.Role;
import br.gov.ce.fortaleza.cti.sgf.service.RoleService;

/**
 * @author Deivid
 * @since 23/01/2010
 */
public class RoleConverter implements Converter{
	
	@Autowired
	private RoleService roleService;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String str) {
		if(str != null && !str.equals("")){
			Role role = roleService.findById(new Integer(str));
			return (Object)role;
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		if(obj == null || obj.equals("")){
			return null;
		}else{
			Role role = (Role) obj;
			return String.valueOf(role.getCodGrupo());
		}
	}

}
