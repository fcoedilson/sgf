/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * @author Deivid
 * @since 19/01/2010
 */
public class EnumConverter implements Converter {

	@SuppressWarnings( { "unchecked", "deprecation" })
	public Object getAsObject(FacesContext context, UIComponent comp, 
			String value) throws ConverterException {
		Class enumType = comp.getValueBinding("value").getType(context);
		return Enum.valueOf(enumType, value);
	}

	public String getAsString(FacesContext context, UIComponent component,
			Object object) throws ConverterException {
		if (object == null) {
			return null;
		}
		Enum<?> type = (Enum<?>) object;
		return type.toString();
	}

}
