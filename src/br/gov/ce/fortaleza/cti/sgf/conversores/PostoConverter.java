/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.gov.ce.fortaleza.cti.sgf.bean.PostoBean;
import br.gov.ce.fortaleza.cti.sgf.entity.Posto;
import br.gov.ce.fortaleza.cti.sgf.service.PostoService;
import br.gov.ce.fortaleza.cti.sgf.util.JSFUtil;

/**
 * @author Deivid
 * @since 11/12/09
 */
public class PostoConverter implements Converter{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String str) {
		// TODO Auto-generated method stub
		if(str != null && !str.equals("")){
			PostoBean postoBean = (PostoBean) JSFUtil.getInstance().getSessionBean("postoBean");
			PostoService postoService = postoBean.getService();
			Posto posto = postoService.retrieve(Integer.valueOf(str));
			return posto;
		}
		return str;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		// TODO Auto-generated method stub
		if(obj == null || obj.equals("")){
			return null;
		}else{
			Posto posto = (Posto) obj;
			return String.valueOf(posto.getCodPosto());
		}
	}

}
