package br.gov.ce.fortaleza.cti.sgf.util;

import java.util.Date;

public class IndexTableDecorator {

	private static long VERDE_INI = 0;
	private static long VERDE_FIM = 10 * DateUtil.MINUTE_IN_MILLIS;
	private static long AMARELO_INI = 10 * DateUtil.MINUTE_IN_MILLIS + 1;
	private static long AMARELO_FIM = 45 * DateUtil.MINUTE_IN_MILLIS;
	private static long VERMELHO_INI = 45 * DateUtil.MINUTE_IN_MILLIS + 1;
	private static long VERMELHO_FIM = 24 * DateUtil.HOUR_IN_MILLIS;
	private static String VERDE = "verde";
	private static String AMARELO = "amarelo";
	private static String VERMELHO = "vermelho";
	private static String CINZA = "cinza";

	public static String selectRowClass(Date data) {
		if(data == null){
			return CINZA;
		}

		Date today = DateUtil.getDateNow();
		long elapsedTime = DateUtil.tempoEntreDatasLong(data, today);

		if (elapsedTime >= VERDE_INI && elapsedTime <= VERDE_FIM) {
			return VERDE;
		} else if (elapsedTime >= AMARELO_INI && elapsedTime <= AMARELO_FIM) {
			return AMARELO;
		} else if (elapsedTime >= VERMELHO_INI && elapsedTime <= VERMELHO_FIM) {
			return VERMELHO;
		} else {
			return CINZA;
		}
	}
}