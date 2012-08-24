/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.gov.ce.fortaleza.cti.sgf.bean.PostoServicoBean;
import br.gov.ce.fortaleza.cti.sgf.entity.PostoServico;
import br.gov.ce.fortaleza.cti.sgf.service.PostoServicoService;
import br.gov.ce.fortaleza.cti.sgf.util.JSFUtil;

/**
 * @author Deivid
 *
 */
public class PostoServicoConverter implements Converter{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String str) {
		// TODO Auto-generated method stub
		if(str != null && !str.equals("")){
			PostoServicoBean postoServicoBean = (PostoServicoBean) JSFUtil.getInstance().getSessionBean("postoServicoBean");
			PostoServicoService postoServicoService = postoServicoBean.getService();
			PostoServico postoServico = postoServicoService.retrieve(Integer.valueOf(str));
			return postoServico;
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		// TODO Auto-generated method stub
		if(obj == null || obj.equals("")){
			return null;
		}else{
			PostoServico postoServico = (PostoServico) obj;
			return String.valueOf(postoServico.getId());
		}
	}

}
