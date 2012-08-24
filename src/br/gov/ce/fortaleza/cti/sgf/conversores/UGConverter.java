package br.gov.ce.fortaleza.cti.sgf.conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.gov.ce.fortaleza.cti.sgf.bean.UGBean;
import br.gov.ce.fortaleza.cti.sgf.entity.UG;
import br.gov.ce.fortaleza.cti.sgf.util.JSFUtil;

/** 
 * @author Deivid
 * @since 29/12/2009
 *
 */

public class UGConverter implements Converter{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String str) {
		// TODO Auto-generated method stub
		if(str != null && !str.equals("")){
			UGBean ugBean = (UGBean) JSFUtil.getInstance().getSessionBean("ugBean");
			UG ug = ugBean.getService().executeSingleResultQuery("findByID", str);
			return ug;
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		// TODO Auto-generated method stub
		if(obj == null || obj.equals("")){
			return null;
		}else{
			UG ug = (UG) obj;
			return String.valueOf(ug.getId());
		}
	}

}
