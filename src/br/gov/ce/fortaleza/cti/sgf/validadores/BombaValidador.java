/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.validadores;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * @author Deivid
 * @since 13/12/2009
 */
public class BombaValidador implements Validator{

	@Override
	public void validate(FacesContext context, UIComponent component, Object obj) throws ValidatorException {
		if(obj.toString().length() < 1 || obj.toString().length() > 2){
			context.addMessage("numero", 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Informe um n�mero entre 1 e 10"));
			throw new ValidatorException(null);
		}
	}
}
