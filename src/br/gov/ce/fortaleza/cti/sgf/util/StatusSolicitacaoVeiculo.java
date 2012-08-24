/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.util;

/**
 * @author Deivid
 * @since 04/02/2010
 */
public enum StatusSolicitacaoVeiculo {
	SOLICITADO, AUTORIZADO, NEGADO, EXTERNO, FINALIZADO;

	@Override
	public String toString() {
		String string = null;
		switch (this) {
		case SOLICITADO:
			string = "Solicitado";
			break;
		case AUTORIZADO:
			string = "Autorizado";
			break;
		case NEGADO:
			string = "Negado";
			break;
		case EXTERNO:
			string = "Externo";
			break;
		case FINALIZADO:
			string = "Finalizado";
			break;
		default:
			string = super.toString();
			break;
		}
		return string;
	}

}
