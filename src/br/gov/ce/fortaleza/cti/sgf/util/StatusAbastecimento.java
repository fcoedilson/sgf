package br.gov.ce.fortaleza.cti.sgf.util;

/**
 * Status que o abastecimento pode ter.
 * 
 * @author joel
 * 
 */
public enum StatusAbastecimento {

	AUTORIZADO, NEGADO, ATENDIDO, NAO_ATENDIDO, VENCIDO, TODOS;

	public String toString() {
		String string = null;
		switch (this) {
		case AUTORIZADO:
			string = "Autorizado";
			break;
		case ATENDIDO:
			string = "Atendido";
			break;
		case NEGADO:
			string = "Negado";
			break;
		case NAO_ATENDIDO:
			string = "NaoAtendido";
			break;			
		case VENCIDO:
			string = "Vencido";
			break;
		case TODOS:
			string = "Todos";
			break;	
		default:
			string = super.toString();
			break;
		}
		return string;
	};

}
