package br.gov.ce.fortaleza.cti.sgf.conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
/**
 * Conversor de valores double (exemplo: moeda)
 * 
 * @author lafitte
 * @since 26/05/2010
 *
 */
public class ValorConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		// TODO Auto-generated method stub
		if(value != null && !value.equals("")){
			String v = value.replace(",", ".");
			Double valor = new Double(v);
			return valor;
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		// TODO Auto-generated method stub
		if (value != null) {
			String valor = value.toString();
			String val = "";
			String fraction = "";
			int indice = 0;
			for (int x = 0; x < valor.toCharArray().length ; x++) {
				if (valor.toCharArray()[x] == '.') {
					indice = x;
				}
			}
			for (int x = 0; x < indice; x++) {
				val += valor.toCharArray()[x];
			}
			for (int x = indice+1; x < valor.toCharArray().length; x++) {
				fraction += valor.toCharArray()[x];
			}
			if (fraction.length() == 1) {
				fraction += "0";
			}
			valor = val+"."+fraction;
			return valor;
		}
		return null;
	}
	
}