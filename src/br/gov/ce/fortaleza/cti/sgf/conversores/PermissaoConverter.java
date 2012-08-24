/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.gov.ce.fortaleza.cti.sgf.entity.Permissao;
import br.gov.ce.fortaleza.cti.sgf.service.PermissaoService;

/**
 * @author Deivid
 * @since 18/01/2010
 */
public class PermissaoConverter implements Converter{

	@Override
	public Object getAsObject(FacesContext contexto, UIComponent componente, String str) {
		if(str != null && !str.equals("")){
			PermissaoService permissaoService = new PermissaoService();
			Permissao permissao = permissaoService.retrieve(Integer.valueOf(str));
			return permissao;
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext contexto, UIComponent componente, Object obj) {
		if(obj == null){
			return null;
		}else{
			Permissao permissao = (Permissao) obj;
			return String.valueOf(permissao.getCodPermissao());
		}
	}
}
