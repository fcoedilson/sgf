/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.gov.ce.fortaleza.cti.sgf.entity.TipoServico;
import br.gov.ce.fortaleza.cti.sgf.service.TipoServicoService;

/**
 * @author Deivid
 * @since 09/12/09
 */
public class TipoServicoConverter implements Converter{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String str) {
		if(str != null && !str.equals("")){
			TipoServicoService service = new TipoServicoService();
			TipoServico tipoServico = service.retrieve(new Integer(str));
			return tipoServico;
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		if(obj == null){
			return null;
		}
		if(obj instanceof TipoServico){
			TipoServico tipoServico = (TipoServico) obj;
			return String.valueOf(tipoServico.getCodTipoServico());  
		}
		return obj.toString();
	}
	
}
